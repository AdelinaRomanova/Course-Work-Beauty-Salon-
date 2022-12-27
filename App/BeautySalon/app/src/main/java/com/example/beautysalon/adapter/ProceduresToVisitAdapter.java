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
import com.example.beautysalon.models.Visit;
import com.example.beautysalon.models.VisitDetail;
import com.example.beautysalon.retrofit.PurchaseService;
import com.example.beautysalon.retrofit.RetrofitService;
import com.example.beautysalon.retrofit.VisitService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProceduresToVisitAdapter extends RecyclerView.Adapter<ProceduresToVisitAdapter.ProceduresToVisitAdapterVH>{
    private List<VisitDetail> visitDetails;
    private Context context;
    Long visit_id;

    public ProceduresToVisitAdapter(List<VisitDetail> visitDetailList, Context context) {
        this.visitDetails = visitDetailList;
        this.context = context;
    }

    public ProceduresToVisitAdapter() {

    }

    public void setDate(List<VisitDetail> visitDetails) {
        this.visitDetails = visitDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProceduresToVisitAdapter.ProceduresToVisitAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProceduresToVisitAdapter.ProceduresToVisitAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_procedures_to_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProceduresToVisitAdapter.ProceduresToVisitAdapterVH holder, @SuppressLint("RecyclerView") int position) {
        VisitDetail visitDetail = visitDetails.get(position);

        String prefix = visitDetail.getProcedureName().substring(0, 1);

        getSelectedPurchaseId();

        holder.prefix.setText(prefix);
        holder.procedureName.setText(visitDetail.getProcedureName());

        holder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitService retrofitService = new RetrofitService();
                VisitService visitService = retrofitService.getRetrofit().create(VisitService.class);
                getSelectedPurchaseId();
                visitService.removeProcedureFromVisit(visit_id, visitDetail.getId())
                        .enqueue(new Callback<VisitDetail>() {
                            @Override
                            public void onResponse(Call<VisitDetail> call, Response<VisitDetail> response) {
                                updateActivity();
                            }

                            @Override
                            public void onFailure(Call<VisitDetail> call, Throwable t) {
                                Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return visitDetails.size();
    }

    private void updateActivity() {
        RetrofitService retrofitService = new RetrofitService();
        VisitService visitService = retrofitService.getRetrofit().create(VisitService.class);
        visitService.getVisitDetails(visit_id)
                .enqueue(new Callback<List<VisitDetail>>() {
                    @Override
                    public void onResponse(Call<List<VisitDetail>> call, Response<List<VisitDetail>> response) {
                        RecyclerView recyclerView = ((Activity)context).findViewById(R.id.recyclerViewProceduresToVisit);
                        ProceduresToVisitAdapter proceduresToVisitAdapter = new ProceduresToVisitAdapter(response.body(), context);
                        recyclerView.setAdapter(proceduresToVisitAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<VisitDetail>> call, Throwable t) {

                    }
                });
    }

    private void getSelectedPurchaseId() {
        RetrofitService retrofitService = new RetrofitService();
        VisitService visitService = retrofitService.getRetrofit().create(VisitService.class);
        visitService.getVisits()
                .enqueue(new Callback<List<Visit>>() {
                    @Override
                    public void onResponse(Call<List<Visit>> call, Response<List<Visit>> response) {
                        visit_id = Long.parseLong(String.valueOf(response.body().get(response.body().size()-1).getId()), 10);
                    }

                    @Override
                    public void onFailure(Call<List<Visit>> call, Throwable t) {

                    }
                });
    }

    public class ProceduresToVisitAdapterVH extends  RecyclerView.ViewHolder {
        TextView procedureName;
        TextView prefix;
        ImageButton btDel;

        public ProceduresToVisitAdapterVH(@NonNull View itemView) {
            super(itemView);
            procedureName = itemView.findViewById(R.id.procedureToVisName);
            prefix = itemView.findViewById(R.id.prefix);

            btDel = (ImageButton) itemView.findViewById(R.id.btDelProcedure);
        }
    }
}
