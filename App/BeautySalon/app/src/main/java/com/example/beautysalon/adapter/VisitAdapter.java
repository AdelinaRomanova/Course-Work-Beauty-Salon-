package com.example.beautysalon.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautysalon.R;
import com.example.beautysalon.models.PurchaseDetail;
import com.example.beautysalon.models.Visit;
import com.example.beautysalon.retrofit.PurchaseService;
import com.example.beautysalon.retrofit.RetrofitService;
import com.example.beautysalon.retrofit.VisitService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.VisitAdapterVH>{
    RetrofitService retrofitService = new RetrofitService();
    VisitService visitService = retrofitService.getRetrofit().create(VisitService.class);

    private List<Visit> visitList;
    private Context context;

    public VisitAdapter() {

    }

    public void setDate(List<Visit> visitList) {
        this.visitList = visitList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VisitAdapter.VisitAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new VisitAdapter.VisitAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_visits, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VisitAdapter.VisitAdapterVH holder, @SuppressLint("RecyclerView") int position) {
        Visit visit = visitList.get(position);

        String visitName = "Посещение №" + visit.getId().toString();
        String string_date = visit.getDate();
        LocalDateTime dateTime = LocalDateTime.parse(string_date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        String date = "Дата посещения: " + dateTime.format(formatter);

        holder.visName.setText(visitName);
        holder.visDate.setText(date);

        holder.btDelVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitService.deleteVisit(visit.getId())
                        .enqueue(new Callback<Visit>() {
                            @Override
                            public void onResponse(Call<Visit> call, Response<Visit> response) {
                                Snackbar.make(v, "Процедура удалена", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                Intent intent = new Intent(context, context.getClass());
                                context.startActivity(intent);
                            }
                            @Override
                            public void onFailure(Call<Visit> call, Throwable t) {
                                Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });

        holder.btViewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVisitUpdateDialog(visit.getId());
            }
        });
    }



    @Override
    public int getItemCount() {
        return visitList.size();
    }

    public class VisitAdapterVH extends  RecyclerView.ViewHolder {
        TextView visName;
        TextView visDate;

        Button btEditVis, btDelVis;
        ImageButton btViewVisit;

        public VisitAdapterVH(@NonNull View itemView) {
            super(itemView);
            visName = itemView.findViewById(R.id.visitName);
            visDate = itemView.findViewById(R.id.visitDate);

            //btEditVis = (Button) itemView.findViewById(R.id.btEditVis);
            btDelVis = (Button) itemView.findViewById(R.id.btDelVis);
            btViewVisit = (ImageButton) itemView.findViewById(R.id.btViewVisit);
        }
    }

    private void showVisitUpdateDialog(Long id) {
        Dialog dialogPurchase = new Dialog(context);
        dialogPurchase.setContentView(R.layout.activity_purchase_update);
        dialogPurchase.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPurchase.setCancelable(true);

        RecyclerView recyclerViewVisitDetails = dialogPurchase.findViewById(R.id.purchaseDetailsList_update);
        recyclerViewVisitDetails.setLayoutManager(new LinearLayoutManager(this.context));
        MaterialButton updateReceipt = dialogPurchase.findViewById(R.id.buttonUpdatePurchase);

        RetrofitService retrofitService = new RetrofitService();
        PurchaseService purchaseService = retrofitService.getRetrofit().create(PurchaseService.class);
        purchaseService.getPurchaseDetails(id)
                .enqueue(new Callback<List<PurchaseDetail>>() {
                    @Override
                    public void onResponse(Call<List<PurchaseDetail>> call, Response<List<PurchaseDetail>> response) {
                        ProceduresViewVisitAdapter proceduresViewVisitAdapter = new ProceduresViewVisitAdapter(response.body(), context);
                        recyclerViewVisitDetails.setAdapter(proceduresViewVisitAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<PurchaseDetail>> call, Throwable t) {

                    }
                });

        updateReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPurchase.cancel();
            }
        });
        dialogPurchase.show();
    }
}
