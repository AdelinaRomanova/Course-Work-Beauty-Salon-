package com.example.beautysalon.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautysalon.R;
import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.models.PurchaseDetail;
import com.example.beautysalon.retrofit.PurchaseService;
import com.example.beautysalon.retrofit.RetrofitService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProceduresToPurchaseAdapter extends RecyclerView.Adapter<ProceduresToPurchaseAdapter.ProceduresToPurchaseAdapterVH>{

    private List<PurchaseDetail> purchaseDetails;
    private Context context;
    private Long purchaseId;

    public ProceduresToPurchaseAdapter(List<PurchaseDetail> receiptDetailsList, Context context) {
        this.purchaseDetails = receiptDetailsList;
        this.context = context;
    }

    public ProceduresToPurchaseAdapter() {

    }

    public void setDate(List<PurchaseDetail> purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProceduresToPurchaseAdapter.ProceduresToPurchaseAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProceduresToPurchaseAdapter.ProceduresToPurchaseAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_procedures_to_purchase, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProceduresToPurchaseAdapter.ProceduresToPurchaseAdapterVH holder, @SuppressLint("RecyclerView") int position) {
        PurchaseDetail purchaseDetail = purchaseDetails.get(position);

        String prefix = purchaseDetail.getProcedureName().substring(0, 1);

        getSelectedPurchaseId();
        holder.prefix.setText(prefix);
        holder.purName.setText(purchaseDetail.getProcedureName());
        holder.count.setText(purchaseDetail.getCount() + " шт.");
        holder.subtotal.setText(String.valueOf(1000 * purchaseDetail.getCount()) + " руб.");

        holder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitService retrofitService = new RetrofitService();
                PurchaseService purchaseService = retrofitService.getRetrofit().create(PurchaseService.class);
                getSelectedPurchaseId();
                purchaseService.removeProcedureFromPurchase(purchaseId, purchaseDetail.getId())
                        .enqueue(new Callback<PurchaseDetail>() {
                            @Override
                            public void onResponse(Call<PurchaseDetail> call, Response<PurchaseDetail> response) {
                                updateActivity();
                            }

                            @Override
                            public void onFailure(Call<PurchaseDetail> call, Throwable t) {
                                Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchaseDetails.size();
    }

    public class ProceduresToPurchaseAdapterVH extends  RecyclerView.ViewHolder {
        TextView purName, count, subtotal;
        TextView prefix;
        ConstraintLayout parentLayout;
        ImageButton btDel;

        public ProceduresToPurchaseAdapterVH(@NonNull View itemView) {
            super(itemView);
            purName = itemView.findViewById(R.id.procedureToPurName);
            prefix = itemView.findViewById(R.id.prefix);
            count = itemView.findViewById(R.id.procedureToPurCount);
            subtotal = itemView.findViewById(R.id.procedureToPurTotal);

            btDel = (ImageButton) itemView.findViewById(R.id.btDel);
        }
    }

    private void updateActivity() {
        RetrofitService retrofitService = new RetrofitService();
        PurchaseService purchaseService = retrofitService.getRetrofit().create(PurchaseService.class);
        purchaseService.getPurchaseDetails(purchaseId)
                .enqueue(new Callback<List<PurchaseDetail>>() {
                    @Override
                    public void onResponse(Call<List<PurchaseDetail>> call, Response<List<PurchaseDetail>> response) {
                        RecyclerView recyclerView = ((Activity)context).findViewById(R.id.recyclerViewProcedures);
                        ProceduresToPurchaseAdapter proceduresToPurchaseAdapter = new ProceduresToPurchaseAdapter(response.body(), context);
                        recyclerView.setAdapter(proceduresToPurchaseAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<PurchaseDetail>> call, Throwable t) {

                    }
                });
    }

    private void getSelectedPurchaseId() {
        RetrofitService retrofitService = new RetrofitService();
        PurchaseService purchaseService = retrofitService.getRetrofit().create(PurchaseService.class);
        purchaseService.getPurchases()
                .enqueue(new Callback<List<Purchase>>() {
                    @Override
                    public void onResponse(Call<List<Purchase>> call, Response<List<Purchase>> response) {
                        purchaseId = Long.parseLong(String.valueOf(response.body().get(response.body().size()-1).getId()), 10);
                    }

                    @Override
                    public void onFailure(Call<List<Purchase>> call, Throwable t) {

                    }
                });
    }
}
