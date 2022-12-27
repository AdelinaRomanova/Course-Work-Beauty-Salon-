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

import com.example.beautysalon.adapter.PurchaseAdapter;
import com.example.beautysalon.adapter.VisitAdapter;
import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.models.Visit;
import com.example.beautysalon.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitsActivity extends AppCompatActivity {
    Button btAdd, btExit;
    RecyclerView recyclerView;

    VisitAdapter visitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);

        recyclerView = findViewById(R.id.recyclerViewVisits);

        btAdd = findViewById(R.id.btAddVisit);
        btExit = findViewById(R.id.btExitMenu);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        visitAdapter = new VisitAdapter();

        getAllVisits();

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisitsActivity.this, VisitAddActivity.class));
                finish();
            }
        });

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisitsActivity.this, UserActivity.class));
                finish();
            }
        });
    }

    public void getAllVisits() {
        Call<List<Visit>> visitList = RetrofitService.getVisitService().getVisits();

        visitList.enqueue(new Callback<List<Visit>>() {
            @Override
            public void onResponse(Call<List<Visit>> call, Response<List<Visit>> response) {
                if(response.isSuccessful()) {
                    List<Visit> visits = response.body();
                    visitAdapter.setDate(visits);
                    recyclerView.setAdapter(visitAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Visit>> call, Throwable t) {
                Toast.makeText(VisitsActivity.this, "Failed to load visits", Toast.LENGTH_SHORT).show();
            }
        });
    }
}