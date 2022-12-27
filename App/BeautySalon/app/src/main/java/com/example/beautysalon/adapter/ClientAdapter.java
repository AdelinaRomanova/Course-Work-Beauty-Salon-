package com.example.beautysalon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautysalon.R;
import com.example.beautysalon.models.Client;
import com.example.beautysalon.models.Procedure;
import com.example.beautysalon.models.Visit;
import com.example.beautysalon.retrofit.ClientService;
import com.example.beautysalon.retrofit.RetrofitService;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientAdapterVH> {
    RetrofitService retrofitService = new RetrofitService();
    ClientService clientService = retrofitService.getRetrofit().create(ClientService.class);

    private List<Client> clientList;
    private Context context;

    public ClientAdapter() {

    }

    public void setDate(List<Client> clientList) {
        this.clientList = clientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ClientAdapter.ClientAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_clients, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapterVH holder, @SuppressLint("RecyclerView") int position) {
        Client client = clientList.get(position);

        String clientFi = client.getName() + " " + client.getSurname();
        String login = client.getLogin();
        int length = client.getPassword().length();
        String password = "";
        for (int i = 0; i < length; i++) {
            password = password + "*";
        }

        holder.clientFi.setText(clientFi);
        holder.clientLogin.setText(login);
        holder.clientPassword.setText(password);

        holder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientService.deleteClient(client.getId())
                        .enqueue(new Callback<Client>() {
                            @Override
                            public void onResponse(Call<Client> call, Response<Client> response) {
                                Snackbar.make(v, "Пользователь удален", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                Intent intent = new Intent(context, context.getClass());
                                context.startActivity(intent);
                            }
                            @Override
                            public void onFailure(Call<Client> call, Throwable t) {
                                Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });

    }



    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public class ClientAdapterVH extends  RecyclerView.ViewHolder {
        TextView clientFi;
        TextView clientLogin;
        TextView clientPassword;

        ImageButton btDel;

        public  ClientAdapterVH(@NonNull View itemView) {
            super(itemView);
            clientFi = itemView.findViewById(R.id.clientFI);
            clientLogin = itemView.findViewById(R.id.clientLogin);
            clientPassword = itemView.findViewById(R.id.clientPassword);

            btDel = (ImageButton) itemView.findViewById(R.id.btDelClient);

        }
    }
}
