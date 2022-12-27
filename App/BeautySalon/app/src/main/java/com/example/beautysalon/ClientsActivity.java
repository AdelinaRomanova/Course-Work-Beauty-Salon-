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

import com.example.beautysalon.adapter.ClientAdapter;
import com.example.beautysalon.models.Client;
import com.example.beautysalon.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsActivity extends AppCompatActivity {
    Button btExit;
    RecyclerView recyclerView;

    ClientAdapter clientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        recyclerView = findViewById(R.id.recyclerViewClients);

        btExit = findViewById(R.id.btExit);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        clientAdapter = new ClientAdapter();

        getAllClients();

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientsActivity.this, AdminActivity.class));
                finish();
            }
        });
    }

    public void getAllClients() {
        Call<List<Client>> clientList = RetrofitService.getClientService().getClients();

        clientList.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if(response.isSuccessful()) {
                    List<Client> clients = response.body();
                    clientAdapter.setDate(clients);
                    recyclerView.setAdapter(clientAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Toast.makeText(ClientsActivity.this, "Ошибка подключения", Toast.LENGTH_SHORT).show();
            }
        });
    }
}