package com.example.beautysalon.retrofit;

import com.example.beautysalon.models.Visit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.4:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static ProcedureService getProcedureService() {
        ProcedureService procedureService = getRetrofit().create(ProcedureService.class);
        return procedureService;
    }

    public static PurchaseService getPurchaseService() {
        PurchaseService purchaseService = getRetrofit().create(PurchaseService.class);
        return purchaseService;
    }

    public static VisitService getVisitService() {
        VisitService visitService = getRetrofit().create(VisitService.class);
        return visitService;
    }

    public static ClientService getClientService() {
        ClientService clientService = getRetrofit().create(ClientService.class);
        return clientService;
    }

}

