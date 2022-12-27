package com.example.beautysalon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.beautysalon.models.Client;
import com.example.beautysalon.retrofit.ClientService;
import com.example.beautysalon.retrofit.RetrofitService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    Button btRegistr;
    EditText edName, edSurname, edLogin, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        RetrofitService retrofitService = new RetrofitService();
        ClientService clientService = retrofitService.getRetrofit().create(ClientService.class);

        btRegistr = findViewById(R.id.buttonRegistration);

        edName = findViewById(R.id.edName);
        edSurname = findViewById(R.id.edSurname);
        edLogin = findViewById(R.id.edLogin);
        edPassword = findViewById(R.id.edPassword);

        btRegistr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edName.getText().toString();
                if (name.isEmpty()) {
                    Snackbar.make(v, "Введите имя!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                String surname = edSurname.getText().toString();
                if (surname.isEmpty()) {
                    Snackbar.make(v, "Введите фамилию!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                String login = edLogin.getText().toString();
                if (login.isEmpty()) {
                    Snackbar.make(v, "Введите логин!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                String password = edPassword.getText().toString();
                if (password.isEmpty()) {
                    Snackbar.make(v, "Введите пароль!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if (password.length() < 3) {
                    Snackbar.make(v, "Слишком короткий пароль!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                Client client = new Client();
                client.setName(name);
                client.setSurname(surname);
                client.setLogin(login);
                client.setPassword(password);

                clientService.addClient(client)
                        .enqueue(new Callback<Client>() {
                            @Override
                            public void onResponse(retrofit2.Call<Client> call, Response<Client> response) {

                                Snackbar.make(v, "Добавлен клиент", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            @Override
                            public void onFailure(Call<Client> call, Throwable t) {
                                Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
                startActivity(new Intent(RegistrationActivity.this, AuthorizationActivity.class));
                finish();
            }
        });
    }


}