package com.example.beautysalon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.beautysalon.adapter.ProcedureAdapter;
import com.example.beautysalon.models.Procedure;
import com.example.beautysalon.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProceduresActivity extends AppCompatActivity {
    Button btAdd, btExit;
    RecyclerView recyclerView;

    ProcedureAdapter procedureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedures);

        recyclerView = findViewById(R.id.recyclerView);

        btAdd = findViewById(R.id.btAdd);
        btExit = findViewById(R.id.btExit);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        procedureAdapter = new ProcedureAdapter();

        getAllProcedures();


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProceduresActivity.this, ProcedureAddActivity.class));
                finish();
            }
        });

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProceduresActivity.this, UserActivity.class));
                finish();
            }
        });

    }

    public void getAllProcedures() {
        Call<List<Procedure>> procedureList = RetrofitService.getProcedureService().getProcedures();

        procedureList.enqueue(new Callback<List<Procedure>>() {
            @Override
            public void onResponse(Call<List<Procedure>> call, Response<List<Procedure>> response) {
                if(response.isSuccessful()) {
                    List<Procedure> procedures = response.body();
                    procedureAdapter.setDate(procedures);
                    recyclerView.setAdapter(procedureAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Procedure>> call, Throwable t) {
                Toast.makeText(ProceduresActivity.this, "Failed to load procedures", Toast.LENGTH_SHORT).show();
            }
        });
    }

}