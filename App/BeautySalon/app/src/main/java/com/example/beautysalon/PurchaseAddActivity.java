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
import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.models.PurchaseDetail;
import com.example.beautysalon.retrofit.PurchaseService;
import com.example.beautysalon.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseAddActivity extends AppCompatActivity {
    Button btAddProcedure, btSave, btExit;
    Dialog dialogProcedure;
    RecyclerView details;
    Long purchase_id;
    ProceduresToPurchaseAdapter proceduresToPurchaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_add);

        btAddProcedure = findViewById(R.id.btAddProcedure);
        btSave = findViewById(R.id.btSave);
        btExit = findViewById(R.id.btExit);
        dialogProcedure = new Dialog(this);

        details = findViewById(R.id.recyclerViewProcedures);

        details.setLayoutManager(new LinearLayoutManager(this));
        details.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        RetrofitService retrofitService = new RetrofitService();
        PurchaseService purchaseService = retrofitService.getRetrofit().create(PurchaseService.class);

        Purchase purchase = new Purchase();
        purchase.setDate(LocalDateTime.now().toString());
        purchase.setTotal(0);

        purchaseService.addPurchases(purchase)
                .enqueue(new Callback<Purchase>() {
                    @Override
                    public void onResponse(Call<Purchase> call, Response<Purchase> response) {
                        purchase_id = response.body().getId();
                    }

                    @Override
                    public void onFailure(Call<Purchase> call, Throwable t) {
                    }
                });

        proceduresToPurchaseAdapter = new ProceduresToPurchaseAdapter();

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
                startActivity(new Intent(PurchaseAddActivity.this, PurchasesActivity.class));
                finish();
            }
        });

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseService.getPurchases()
                        .enqueue(new Callback<List<Purchase>>() {
                            @Override
                            public void onResponse(Call<List<Purchase>> call, Response<List<Purchase>> response) {
                                List<Purchase> purchaseList = response.body();
                                Long last_id = purchaseList.get(purchaseList.size()-1).getId();
                                purchaseService.deletePurchases(last_id)
                                        .enqueue(new Callback<Purchase>() {
                                            @Override
                                            public void onResponse(Call<Purchase> call, Response<Purchase> response) {
                                                startActivity(new Intent(PurchaseAddActivity.this, PurchasesActivity.class));
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<Purchase> call, Throwable t) {

                                            }
                                        });
                            }

                            @Override
                            public void onFailure(Call<List<Purchase>> call, Throwable t) {
                            }
                        });
            }
        });

    }

    private void showProcedureSelectedDialog(View mainView) {
        dialogProcedure.setContentView(R.layout.activity_select_procedure_to_purchase);
        dialogProcedure.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProcedure.setCancelable(true);

        EditText inputName = dialogProcedure.findViewById(R.id.procedure_name);
        EditText inputCount = dialogProcedure.findViewById(R.id.procedure_price);
        MaterialButton saveProcedure = dialogProcedure.findViewById(R.id.buttonSaveCosmetic);
        MaterialButton cancel = dialogProcedure.findViewById(R.id.buttonCancel);

        RetrofitService retrofitService = new RetrofitService();
        PurchaseService receiptApi = retrofitService.getRetrofit().create(PurchaseService.class);

        saveProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                if (name.isEmpty()) {
                    Snackbar.make(mainView, "Введите название", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                int count;
                try {
                    count = Integer.parseInt(inputCount.getText().toString());
                } catch (NumberFormatException nfe) {
                    Snackbar.make(mainView, "Некорректное значение", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if (count < 0) {
                    Snackbar.make(mainView, "Не имеет смысла :)", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                receiptApi.addProcedureToPurchase(purchase_id, Long.parseLong(name, 10), count)
                        .enqueue(new Callback<PurchaseDetail>() {
                            @Override
                            public void onResponse(Call<PurchaseDetail> call, Response<PurchaseDetail> response) {
                                dialogProcedure.cancel();
//                                startActivity(new Intent(PurchaseAddActivity.this, PurchasesActivity.class));
//                                finish();
                                getAllProcedures();
                            }

                            @Override
                            public void onFailure(Call<PurchaseDetail> call, Throwable t) {
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
        Call<List<PurchaseDetail>> procedures = RetrofitService.getPurchaseService().getPurchaseDetails(purchase_id);

        procedures.enqueue(new Callback<List<PurchaseDetail>>() {
            @Override
            public void onResponse(Call<List<PurchaseDetail>> call, Response<List<PurchaseDetail>> response) {
                if(response.isSuccessful()) {
                    List<PurchaseDetail> procedures = response.body();
                    proceduresToPurchaseAdapter.setDate(procedures);
                    details.setAdapter(proceduresToPurchaseAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseDetail>> call, Throwable t) {
                dialogProcedure.cancel();
            }
        });
    }

}