package com.example.beautysalon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btProcedures = findViewById(R.id.btListProcedures);
        Button btPurchases = findViewById(R.id.btListPurchases);
        Button btVisits = findViewById(R.id.btListVisits);
        Button btListClients = findViewById(R.id.btListClients);
        ImageButton btExitMenu = findViewById(R.id.btExitMenu);

        btProcedures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ProceduresActivity.class));
            }
        });

        btPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, PurchasesActivity.class));
            }
        });

        btVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, VisitsActivity.class));
            }
        });

        btListClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ClientsActivity.class));
            }
        });

        btExitMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AuthorizationActivity.class));
            }
        });
    }


}