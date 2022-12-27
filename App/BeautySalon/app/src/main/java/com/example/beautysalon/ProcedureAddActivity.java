package com.example.beautysalon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.beautysalon.models.Procedure;
import com.example.beautysalon.retrofit.RetrofitService;
import com.example.beautysalon.retrofit.ProcedureService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcedureAddActivity extends AppCompatActivity {
    Button btSave, btExit;
    EditText edName, edPrice, edMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RetrofitService retrofitService = new RetrofitService();
        ProcedureService procedureService = retrofitService.getRetrofit().create(ProcedureService.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure_add);

        btSave = findViewById(R.id.btSave);
        btExit = findViewById(R.id.btExit);

        edName = findViewById(R.id.edName);
        edPrice = findViewById(R.id.edPrice);
        edMaster = findViewById(R.id.edMaster);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                if (name.isEmpty()) {
                    Snackbar.make(v, "Введите название!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                String master = edMaster.getText().toString();
                if (master.isEmpty()) {
                    Snackbar.make(v, "Введите мастера!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                float price;
                try
                {
                    price = Float.parseFloat(edPrice.getText().toString());
                }
                catch (NumberFormatException nfe)
                {
                    Snackbar.make(v, "Некорректное значение", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if (edPrice.getText().toString().isEmpty()) {
                    price = 0;
                }
                if (price < 0) {
                    Snackbar.make(v, "Некорректное значение", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                Procedure procedure = new Procedure();
                procedure.setName(name);
                procedure.setMaster(master);
                procedure.setPrice(price);

                procedureService.addProcedure(procedure)
                        .enqueue(new Callback<Procedure>() {
                            @Override
                            public void onResponse(Call<Procedure> call, Response<Procedure> response) {

                                Snackbar.make(v, "Добавлена процедура", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            @Override
                            public void onFailure(Call<Procedure> call, Throwable t) {
                                Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
                startActivity(new Intent(ProcedureAddActivity.this, ProceduresActivity.class));
                finish();
            }
        });

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProcedureAddActivity.this, UserActivity.class));
                finish();
            }
        });
    }
}
