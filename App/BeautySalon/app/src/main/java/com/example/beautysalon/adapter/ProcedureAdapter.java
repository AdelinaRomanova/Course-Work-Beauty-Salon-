package com.example.beautysalon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautysalon.R;
import com.example.beautysalon.retrofit.RetrofitService;
import com.example.beautysalon.retrofit.ProcedureService;
import com.example.beautysalon.models.Procedure;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcedureAdapter extends RecyclerView.Adapter<ProcedureAdapter.ProcedureAdapterVH> {
    RetrofitService retrofitService = new RetrofitService();
    ProcedureService procedureService = retrofitService.getRetrofit().create(ProcedureService.class);

    private List<Procedure> procedureList;
    private Context context;

    public ProcedureAdapter() {

    }

    public void setDate(List<Procedure> procedureList) {
        this.procedureList = procedureList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProcedureAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProcedureAdapter.ProcedureAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_procedures, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProcedureAdapterVH holder, @SuppressLint("RecyclerView") int position) {
        Procedure procedure = procedureList.get(position);

        String procedureName = procedure.getName();
        String prefix = procedure.getName().substring(0, 1);
        float i = procedure.getPrice();
        String price = Float.toString(i);

        holder.prefix.setText(prefix);
        holder.procedureName.setText(procedureName);
        holder.price.setText(price);

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.update_procedure))
                        .setExpanded(true, 1200)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText name = view.findViewById(R.id.editName);
                EditText master = view.findViewById(R.id.editMaster);
                EditText price = view.findViewById(R.id.editPrice);

                Button btSave = view.findViewById(R.id.btSave);

                name.setText(procedure.getName());
                master.setText(procedure.getMaster());
                price.setText(String.valueOf(procedure.getPrice()));

                dialogPlus.show();

                btSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Procedure updProcedure = new Procedure();
                        updProcedure.setName(name.getText().toString());
                        updProcedure.setMaster(master.getText().toString());
                        updProcedure.setPrice(Float.parseFloat(price.getText().toString()));

                        procedureService.editProcedure(procedure.getId(), updProcedure)
                                .enqueue(new Callback<Procedure>() {
                                    @Override
                                    public void onResponse(Call<Procedure> call, Response<Procedure> response) {
                                        Snackbar.make(v, "Сохранено", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        dialogPlus.dismiss();
                                        Intent intent = new Intent(context, context.getClass());
                                        context.startActivity(intent);
                                    }
                                    @Override
                                    public void onFailure(Call<Procedure> call, Throwable t) {
                                        Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }

        });

        holder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procedureService.deleteProcedure(procedure.getId())
                        .enqueue(new Callback<Procedure>() {
                            @Override
                            public void onResponse(Call<Procedure> call, Response<Procedure> response) {
                                Snackbar.make(v, "Процедура удалена", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                Intent intent = new Intent(context, context.getClass());
                                context.startActivity(intent);
                            }
                            @Override
                            public void onFailure(Call<Procedure> call, Throwable t) {
                                Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });
    }



    @Override
    public int getItemCount() {
        return procedureList.size();
    }

    public class ProcedureAdapterVH extends  RecyclerView.ViewHolder {
        TextView procedureName;
        TextView prefix;
        TextView price;

        Button btEdit, btDel;

        public  ProcedureAdapterVH(@NonNull View itemView) {
            super(itemView);
            procedureName = itemView.findViewById(R.id.procedureName);
            prefix = itemView.findViewById(R.id.prefix);
            price = itemView.findViewById(R.id.procedurePrice);

            btEdit = (Button) itemView.findViewById(R.id.btEdit);
            btDel = (Button) itemView.findViewById(R.id.btDel);

        }
    }
}
