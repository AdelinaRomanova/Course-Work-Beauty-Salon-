package com.example.beautysalon.retrofit;

import com.example.beautysalon.models.Client;
import com.example.beautysalon.models.Procedure;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ClientService {
    @GET("/clients")
    Call<List<Client>> getClients();

    @POST("/clients")
    Call<Client> addClient(@Body Client client);

    @DELETE("/clients/{id}")
    Call<Client> deleteClient(@Path("id") Long id);
}
