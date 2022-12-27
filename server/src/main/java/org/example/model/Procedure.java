package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.model.dto.ProcedureDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "procedures")
public class Procedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String master;
    private float price;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "procedure", cascade = CascadeType.REMOVE)
    private List<PurchaseDetail> purchaseDetails = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "procedure", cascade = CascadeType.REMOVE)
    private List<VisitDetail> visitDetails = new ArrayList<>();


    public Procedure() { }

    public static Procedure from(ProcedureDto procedureDto) {
        Procedure procedure = new Procedure();
        procedure.setName(procedureDto.getName());
        procedure.setMaster(procedureDto.getMaster());
        procedure.setPrice(procedureDto.getPrice());
        return procedure;
    }

    public void addPurchase(PurchaseDetail receipt) {
        this.purchaseDetails.add(receipt);
    }
    public void addProcedurePurchase(PurchaseDetail procedurePurchase) { this.purchaseDetails.add(procedurePurchase); }

    public void addVisit(VisitDetail visitDetail) {
        this.visitDetails.add(visitDetail);
    }
    public void addProcedureVisit(VisitDetail visitDetail) { this.visitDetails.add(visitDetail); }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<PurchaseDetail> getPurchaseDetails() {
        return purchaseDetails;
    }

    public void setPurchaseDetails(List<PurchaseDetail> purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
    }

    public List<VisitDetail> getVisitDetails() {
        return visitDetails;
    }

    public void setVisitDetails(List<VisitDetail> visitDetails) {
        this.visitDetails = visitDetails;
    }
}
