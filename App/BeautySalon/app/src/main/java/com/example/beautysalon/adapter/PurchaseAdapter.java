package com.example.beautysalon.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautysalon.R;
import com.example.beautysalon.models.Purchase;
import com.example.beautysalon.models.PurchaseDetail;
import com.example.beautysalon.retrofit.PurchaseService;
import com.example.beautysalon.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseAdapterVH>{
    RetrofitService retrofitService = new RetrofitService();
    PurchaseService purchaseService = retrofitService.getRetrofit().create(PurchaseService.class);

    private List<Purchase> purchaseList;
    private Context context;

    public PurchaseAdapter() {

    }

    public void setDate(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PurchaseAdapter.PurchaseAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new PurchaseAdapter.PurchaseAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_purchases, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.PurchaseAdapterVH holder, @SuppressLint("RecyclerView") int position) {
        Purchase purchase = purchaseList.get(position);

        String purchaseName = "Покупка №" + purchase.getId().toString();
        String string_date = purchase.getDate();
        LocalDateTime dateTime = LocalDateTime.parse(string_date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        String date = "Дата покупки: " + dateTime.format(formatter);
        float i = purchase.getTotal();
        String price = Float.toString(i);

        holder.purName.setText(purchaseName);
        holder.purDate.setText(date);
        holder.purTotalCost.setText(price);

        holder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseService.deletePurchases(purchase.getId())
                        .enqueue(new Callback<Purchase>() {
                            @Override
                            public void onResponse(Call<Purchase> call, Response<Purchase> response) {
                                Snackbar.make(v, "Процедура удалена", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                Intent intent = new Intent(context, context.getClass());
                                context.startActivity(intent);
                            }
                            @Override
                            public void onFailure(Call<Purchase> call, Throwable t) {
                                Snackbar.make(v, "Ошибка подключения", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });

        holder.btReportPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument myPdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint forLinePain = new Paint();

                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(250, 250,1).create();
                PdfDocument.Page  myPage1 = myPdfDocument.startPage(myPageInfo1);

                Canvas canvas = myPage1.getCanvas();

                paint.setTextSize(15.5f);
                paint.setColor(Color.rgb(0,0,0));
                canvas.drawText("Салон красоты 'Вы ужасны!'", 20, 20, paint);

                paint.setTextSize(8.5f);
                canvas.drawText("Чек покупки №" + purchase.getId().toString() , 20, 40, paint);

                forLinePain.setStyle(Paint.Style.STROKE);
                forLinePain.setPathEffect(new DashPathEffect(new float[]{5,5}, 0));
                forLinePain.setStrokeWidth(2);
                canvas.drawLine(20,50,230, 50, forLinePain);

                canvas.drawText("Процедуры: ", 20, 70, paint);

                int idProcedure = 1;
                int y = 85;
                int yV = 15;
                List<PurchaseDetail> procedures = purchase.getProcedures();

                for (PurchaseDetail ps : procedures) {
                    canvas.drawText(idProcedure + ". " + ps.getProcedureName(), 20, y, paint);
                    idProcedure++;
                    canvas.drawText(ps.getCount() + " шт,       " + ps.getSubtotal() + " руб", 145, y, paint);
                    y+=yV;
                }

                forLinePain.setStyle(Paint.Style.STROKE);
                forLinePain.setPathEffect(new DashPathEffect(new float[]{5,5}, 0));
                forLinePain.setStrokeWidth(2);
                y += 3;
                canvas.drawLine(20,y,230, y, forLinePain);

                y +=yV;
                canvas.drawText("Итого: " + purchase.getTotal() + " руб", 150, y, paint);

                y +=yV+10;
                canvas.drawText(date , 20, y, paint);

                paint.setTextSize(10.5f);
                y +=yV+10;
                canvas.drawText("Спасибо за покупку!" , 70, y, paint);

                myPdfDocument.finishPage(myPage1);

                File file = new File("/storage/emulated/0/Download/PDF_REP_" + "Purchase" + purchase.getId().toString() + ".pdf");


                try {
                    myPdfDocument.writeTo((new FileOutputStream(file)));
                    Snackbar.make(v, "Отчёт создан!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myPdfDocument.close();
            }
        });

        holder.btViewPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPurchaseUpdateDialog(purchase.getId());
            }
        });
    }

    private void showPurchaseUpdateDialog(Long id) {
        Dialog dialogPurchase = new Dialog(context);
        dialogPurchase.setContentView(R.layout.activity_purchase_update);
        dialogPurchase.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPurchase.setCancelable(true);

        RecyclerView recyclerViewPurchaseDetails = dialogPurchase.findViewById(R.id.purchaseDetailsList_update);
        recyclerViewPurchaseDetails.setLayoutManager(new LinearLayoutManager(this.context));
        MaterialButton updateReceipt = dialogPurchase.findViewById(R.id.buttonUpdatePurchase);

        RetrofitService retrofitService = new RetrofitService();
        PurchaseService purchaseService = retrofitService.getRetrofit().create(PurchaseService.class);
        purchaseService.getPurchaseDetails(id)
                .enqueue(new Callback<List<PurchaseDetail>>() {
                    @Override
                    public void onResponse(Call<List<PurchaseDetail>> call, Response<List<PurchaseDetail>> response) {
                        ProceduresViewProcedureAdapter proceduresViewProcedureAdapter = new ProceduresViewProcedureAdapter(response.body(), context);
                        recyclerViewPurchaseDetails.setAdapter(proceduresViewProcedureAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<PurchaseDetail>> call, Throwable t) {

                    }
                });

        updateReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPurchase.cancel();
            }
        });
        dialogPurchase.show();
    }



    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public class PurchaseAdapterVH extends  RecyclerView.ViewHolder {
        TextView purName;
        TextView purDate;
        TextView purTotalCost;

        Button btDel, btEdit;
        ImageButton btReportPurchase, btViewPurchase;

        public PurchaseAdapterVH(@NonNull View itemView) {
            super(itemView);
            purName = itemView.findViewById(R.id.purchaseName);
            purDate = itemView.findViewById(R.id.purchaseDate);
            purTotalCost = itemView.findViewById(R.id.purchaseTotalCost);

            btEdit = (Button) itemView.findViewById(R.id.btEdit1);
            btDel = (Button) itemView.findViewById(R.id.btDel1);
            btReportPurchase = (ImageButton) itemView.findViewById(R.id.btReportPurchase);
            btViewPurchase = (ImageButton) itemView.findViewById(R.id.btViewPurchase);
        }
    }

}
