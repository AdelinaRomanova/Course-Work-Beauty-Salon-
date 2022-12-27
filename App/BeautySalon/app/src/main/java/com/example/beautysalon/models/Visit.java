package com.example.beautysalon.models;

import java.util.ArrayList;
import java.util.List;

public class Visit {
    private Long id;
    private String date;
    private List<VisitDetail> visits = new ArrayList<>();

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

    public List<VisitDetail> getVisits() {
        return visits;
    }

    public void setVisits(List<VisitDetail> visits) {
        this.visits = visits;
    }
}
