package com.example.beautysalon.retrofit;

import com.example.beautysalon.models.Procedure;
import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.models.PurchaseDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PurchaseService {
    @GET("/purchases")
    Call<List<Purchase>> getPurchases();

    @POST("/purchases")
    Call<Purchase> addPurchases(@Body Purchase purchase);

    @PUT("/purchases/{id}")
    Call<Purchase> editPurchases(@Path("id") Long id, @Body Purchase purchase);

    @GET("/purchases/{id}/procedures")
    Call<List<PurchaseDetail>> getPurchaseDetails(@Path("id") Long id);

    @DELETE("/purchases/{id}")
    Call<Purchase> deletePurchases(@Path("id") Long id);

    @POST("/purchases/{purchaseId}/procedures/add/{procedureId}/{count}")
    Call<PurchaseDetail> addProcedureToPurchase(@Path("purchaseId") Long purchase_id,
                                                @Path("procedureId") Long procedure_id,
                                                @Path("count") int count);

    @DELETE("/purchases/{purchaseId}/procedures/remove/{procedureId}")
    Call<PurchaseDetail> removeProcedureFromPurchase(@Path("purchaseId") Long purchase_id,
                                                     @Path("procedureId") Long procedure_id);
}
