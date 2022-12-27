package com.example.beautysalon.retrofit;

import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.models.PurchaseDetail;
import com.example.beautysalon.models.Visit;
import com.example.beautysalon.models.VisitDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VisitService {
    @GET("/visits")
    Call<List<Visit>> getVisits();

    @POST("/visits")
    Call<Visit> addVisit(@Body Visit visit);

    @PUT("/visits/{id}")
    Call<Visit> editVisit(@Path("id") Long id, @Body Visit visit);

    @GET("/visits/{id}/procedures")
    Call<List<VisitDetail>> getVisitDetails(@Path("id") Long id);

    @DELETE("/visits/{id}")
    Call<Visit> deleteVisit(@Path("id") Long id);

    @POST("/visits/{visitId}/procedures/add/{procedureId}")
    Call<VisitDetail> addProcedureToVisit(@Path("visitId") Long visit_id,
                                                @Path("procedureId") Long procedure_id);

    @DELETE("/visits/{visitId}/procedures/remove/{procedureId}")
    Call<VisitDetail> removeProcedureFromVisit(@Path("visitId") Long visit_id,
                                                     @Path("procedureId") Long procedure_id);
}
