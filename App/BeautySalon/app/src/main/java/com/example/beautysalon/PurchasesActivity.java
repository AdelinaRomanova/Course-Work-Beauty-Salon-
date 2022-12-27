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
import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.retrofit.PurchaseService;
import com.example.beautysalon.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchasesActivity extends AppCompatActivity {
    Button btAdd, btExit;
    RecyclerView recyclerView;

    PurchaseAdapter purchaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        recyclerView = findViewById(R.id.recyclerView);

        btAdd = findViewById(R.id.btAdd);
        btExit = findViewById(R.id.btExit);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        purchaseAdapter = new PurchaseAdapter();

        getAllPurchases();


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PurchasesActivity.this, PurchaseAddActivity.class));
                finish();
            }
        });

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PurchasesActivity.this, UserActivity.class));
                finish();
            }
        });
    }

    public void getAllPurchases() {
        Call<List<Purchase>> purchaseList = RetrofitService.getPurchaseService().getPurchases();

        purchaseList.enqueue(new Callback<List<Purchase>>() {
            @Override
            public void onResponse(Call<List<Purchase>> call, Response<List<Purchase>> response) {
                if(response.isSuccessful()) {
                    List<Purchase> purchases = response.body();
                    purchaseAdapter.setDate(purchases);
                    recyclerView.setAdapter(purchaseAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Purchase>> call, Throwable t) {
                Toast.makeText(PurchasesActivity.this, "Failed to load procedures", Toast.LENGTH_SHORT).show();
            }
        });
    }
}