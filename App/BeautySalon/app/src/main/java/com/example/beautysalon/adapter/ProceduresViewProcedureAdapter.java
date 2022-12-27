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

public class ProceduresViewProcedureAdapter extends RecyclerView.Adapter<ProceduresViewProcedureAdapter.ProceduresViewAdapterVH>{

    private List<PurchaseDetail> purchaseDetails;
    private Context context;
    private Long purchaseId;

    public ProceduresViewProcedureAdapter(List<PurchaseDetail> receiptDetailsList, Context context) {
        this.purchaseDetails = receiptDetailsList;
        this.context = context;
    }

    public ProceduresViewProcedureAdapter() {

    }

    public void setDate(List<PurchaseDetail> purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProceduresViewProcedureAdapter.ProceduresViewAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProceduresViewProcedureAdapter.ProceduresViewAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_procedures_view_purchase, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProceduresViewProcedureAdapter.ProceduresViewAdapterVH holder, @SuppressLint("RecyclerView") int position) {
        PurchaseDetail purchaseDetail = purchaseDetails.get(position);

        String prefix = purchaseDetail.getProcedureName().substring(0, 1);

        getSelectedPurchaseId();
        holder.prefix.setText(prefix);
        holder.purName.setText(purchaseDetail.getProcedureName());
        holder.count.setText(purchaseDetail.getCount() + " шт.");
        holder.subtotal.setText(String.valueOf(1000 * purchaseDetail.getCount()) + " руб.");
    }

    @Override
    public int getItemCount() {
        return purchaseDetails.size();
    }

    public class ProceduresViewAdapterVH extends  RecyclerView.ViewHolder {
        TextView purName, count, subtotal;
        TextView prefix;
        ConstraintLayout parentLayout;

        public ProceduresViewAdapterVH(@NonNull View itemView) {
            super(itemView);
            purName = itemView.findViewById(R.id.procedureToPurViewName);
            prefix = itemView.findViewById(R.id.prefixView);
            count = itemView.findViewById(R.id.procedureToPurViewCount);
            subtotal = itemView.findViewById(R.id.procedureToPurViewTotal);
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
