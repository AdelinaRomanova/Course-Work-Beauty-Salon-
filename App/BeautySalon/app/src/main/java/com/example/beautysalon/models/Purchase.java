package com.example.beautysalon.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Purchase {
    private Long id;
    private String date;
    private float total;
    private List<PurchaseDetail> procedures = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<PurchaseDetail> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<PurchaseDetail> procedures) {
        this.procedures = procedures;
    }
}
