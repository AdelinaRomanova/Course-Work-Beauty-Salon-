package com.example.beautysalon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautysalon.R;
import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.models.PurchaseDetail;
import com.example.beautysalon.retrofit.PurchaseService;
import com.example.beautysalon.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProceduresViewVisitAdapter extends RecyclerView.Adapter<ProceduresViewVisitAdapter.ProceduresViewVisitAdapterVH>{

    private List<PurchaseDetail> purchaseDetails;
    private Context context;
    private Long purchaseId;

    public ProceduresViewVisitAdapter(List<PurchaseDetail> receiptDetailsList, Context context) {
        this.purchaseDetails = receiptDetailsList;
        this.context = context;
    }

    public ProceduresViewVisitAdapter() {

    }

    public void setDate(List<PurchaseDetail> purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProceduresViewVisitAdapter.ProceduresViewVisitAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProceduresViewVisitAdapter.ProceduresViewVisitAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_procedures_view_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProceduresViewVisitAdapter.ProceduresViewVisitAdapterVH holder, @SuppressLint("RecyclerView") int position) {
        PurchaseDetail purchaseDetail = purchaseDetails.get(position);

        String prefix = purchaseDetail.getProcedureName().substring(0, 1);
        String procedureName = purchaseDetail.getProcedureName();

        getSelectedPurchaseId();
        holder.prefix.setText(prefix);
        holder.procedureName.setText(procedureName);
    }

    @Override
    public int getItemCount() {
        return purchaseDetails.size();
    }

    public class ProceduresViewVisitAdapterVH extends  RecyclerView.ViewHolder {
        TextView procedureName;
        TextView prefix;

        public ProceduresViewVisitAdapterVH(@NonNull View itemView) {
            super(itemView);
            procedureName = itemView.findViewById(R.id.procedureToVisName);
            prefix = itemView.findViewById(R.id.prefix);
        }
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
