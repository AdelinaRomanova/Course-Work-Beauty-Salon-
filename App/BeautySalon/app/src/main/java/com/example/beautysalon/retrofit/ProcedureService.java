package com.example.beautysalon.retrofit;

import com.example.beautysalon.models.Procedure;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProcedureService {
    @GET("/procedures")
    Call<List<Procedure>> getProcedures();

    @POST("/procedures")
    Call<Procedure> addProcedure(@Body Procedure procedure);

    @PUT("/procedures/{id}")
    Call<Procedure> editProcedure(@Path("id") Long id, @Body Procedure procedure);

    @DELETE("/procedures/{id}")
    Call<Procedure> deleteProcedure(@Path("id") Long id);
}
