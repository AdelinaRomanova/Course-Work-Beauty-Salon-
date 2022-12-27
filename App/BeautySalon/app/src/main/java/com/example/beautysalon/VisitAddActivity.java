package com.example.beautysalon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.beautysalon.adapter.ProceduresToPurchaseAdapter;
import com.example.beautysalon.adapter.ProceduresToVisitAdapter;
import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.models.PurchaseDetail;
import com.example.beautysalon.models.Visit;
import com.example.beautysalon.models.VisitDetail;
import com.example.beautysalon.retrofit.PurchaseService;
import com.example.beautysalon.retrofit.RetrofitService;
import com.example.beautysalon.retrofit.VisitService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitAddActivity extends AppCompatActivity {

    Button btAddProcedure, btSave, btExit;
    Dialog dialogProcedure;
    RecyclerView details;
    Long visit_id;
    ProceduresToVisitAdapter proceduresToVisitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_add);

        btAddProcedure = findViewById(R.id.btAddProcedureToVisit);
        btSave = findViewById(R.id.btSaveVisit);
        btExit = findViewById(R.id.btExitVisit);
        dialogProcedure = new Dialog(this);

        details = findViewById(R.id.recyclerViewProceduresToVisit);

        details.setLayoutManager(new LinearLayoutManager(this));
        details.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        RetrofitService retrofitService = new RetrofitService();
        VisitService visitService = retrofitService.getRetrofit().create(VisitService.class);

        Visit visit = new Visit();
        visit.setDate(LocalDateTime.now().toString());

        visitService.addVisit(visit)
                .enqueue(new Callback<Visit>() {
                    @Override
                    public void onResponse(Call<Visit> call, Response<Visit> response) {
                        visit_id = response.body().getId();
                    }

                    @Override
                    public void onFailure(Call<Visit> call, Throwable t) {
                    }
                });

        proceduresToVisitAdapter = new ProceduresToVisitAdapter();

        //getAllProcedures();

        btAddProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProcedureSelectedDialog(view);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisitAddActivity.this, VisitsActivity.class));
                finish();
            }
        });

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitService.getVisits()
                        .enqueue(new Callback<List<Visit>>() {
                            @Override
                            public void onResponse(Call<List<Visit>> call, Response<List<Visit>> response) {
                                List<Visit> visitList = response.body();
                                Long last_id = visitList.get(visitList.size()-1).getId();
                                visitService.deleteVisit(last_id)
                                        .enqueue(new Callback<Visit>() {
                                            @Override
                                            public void onResponse(Call<Visit> call, Response<Visit> response) {
                                                startActivity(new Intent(VisitAddActivity.this, VisitsActivity.class));
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<Visit> call, Throwable t) {

                                            }
                                        });
                            }

                            @Override
                            public void onFailure(Call<List<Visit>> call, Throwable t) {
                            }
                        });
            }
        });

    }

    private void showProcedureSelectedDialog(View mainView) {
        dialogProcedure.setContentView(R.layout.activity_select_procedure_to_visit);
        dialogProcedure.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProcedure.setCancelable(true);

        EditText inputName = dialogProcedure.findViewById(R.id.procedure_id);
        MaterialButton saveProcedure = dialogProcedure.findViewById(R.id.buttonSaveProcedure);
        MaterialButton cancel = dialogProcedure.findViewById(R.id.buttonCancel);

        RetrofitService retrofitService = new RetrofitService();
        VisitService receiptApi = retrofitService.getRetrofit().create(VisitService.class);

        saveProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                if (name.isEmpty()) {
                    Snackbar.make(mainView, "Введите идентификатор", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                receiptApi.addProcedureToVisit(visit_id, Long.parseLong(name, 10))
                        .enqueue(new Callback<VisitDetail>() {
                            @Override
                            public void onResponse(Call<VisitDetail> call, Response<VisitDetail> response) {
                                dialogProcedure.cancel();
//                                startActivity(new Intent(PurchaseAddActivity.this, PurchasesActivity.class));
//                                finish();
                                getAllProcedures();
                            }

                            @Override
                            public void onFailure(Call<VisitDetail> call, Throwable t) {
                                dialogProcedure.cancel();
                                Snackbar.make(mainView, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }


        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProcedure.cancel();
            }
        });
        dialogProcedure.show();
    }

    public void getAllProcedures() {
        Call<List<VisitDetail>> procedures = RetrofitService.getVisitService().getVisitDetails(visit_id);

        procedures.enqueue(new Callback<List<VisitDetail>>() {
            @Override
            public void onResponse(Call<List<VisitDetail>> call, Response<List<VisitDetail>> response) {
                if(response.isSuccessful()) {
                    List<VisitDetail> procedures = response.body();
                    proceduresToVisitAdapter.setDate(procedures);
                    details.setAdapter(proceduresToVisitAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<VisitDetail>> call, Throwable t) {
                dialogProcedure.cancel();
            }
        });
    }
}