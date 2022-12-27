package com.example.beautysalon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beautysalon.models.Client;
import com.example.beautysalon.retrofit.RetrofitService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizationActivity extends AppCompatActivity {
    Button btnAuthor, btRegistr;
    EditText edLogin, edPassword;

    List<Client> clientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        btnAuthor = findViewById(R.id.btAuto);
        btRegistr = findViewById(R.id.buttonRegistration);

        edLogin = findViewById(R.id.edLogin);
        edPassword = findViewById(R.id.edPassword);

        getClients();

        btRegistr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthorizationActivity.this, RegistrationActivity.class));
                finish();
            }
        });

        btnAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = false;

                String login="";
                String password="";

                String loginClient = edLogin.getText().toString();
                if (loginClient.isEmpty()) {
                    Snackbar.make(view, "Введите логин!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                String passwordClient = edPassword.getText().toString();
                if (passwordClient.isEmpty()) {
                    Snackbar.make(view, "Введите пароль!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (loginClient.equals("admin") && passwordClient.equals("admin")) {
                    Intent changeActivity = new Intent(AuthorizationActivity.this, AdminActivity.class);
                    startActivity(changeActivity);
                } else {

                    for (int i = 0; i < clientsList.size(); i++) {
                        login = clientsList.get(i).getLogin();
                        password = clientsList.get(i).getPassword();

                        Snackbar.make(view,   login + " " + password, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        if (login.equals(loginClient) && password.equals(passwordClient)) {
                            Intent changeActivity = new Intent(AuthorizationActivity.this, UserActivity.class);
                            startActivity(changeActivity);
                            success = true;
                            break;
                        }
                    }
                    if (success == false) {
                        Snackbar.make(view, "Такого пользователя не существует!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

    }

    public void getClients() {
        Call<List<Client>> clients = RetrofitService.getClientService().getClients();

        clients.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if(response.isSuccessful()) {
                    clientsList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Toast.makeText(AuthorizationActivity.this, "Ошибка подключения!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}