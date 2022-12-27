package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.model.dto.PurchaseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private float total;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseDetail> procedures = new ArrayList<>();

    public Purchase() { }

    public static Purchase from(PurchaseDto purchaseDto) {
        Purchase purchase = new Purchase();
        purchase.setDate(purchaseDto.getDate());
        purchase.setTotal(purchaseDto.getTotal());
        purchase.setProcedures(purchaseDto.getProcedures().stream().map(PurchaseDetail::from).collect(Collectors.toList()));
        return purchase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<PurchaseDetail> getProcedures() { return procedures; }

    public void setProcedures(List<PurchaseDetail> procedures) {
        this.procedures = procedures;
    }

    public void addProcedurePurchase(PurchaseDetail purchaseDetail) {
        procedures.add(purchaseDetail);
    }

    public void removeProcedurePurchase(PurchaseDetail purchaseDetail) { procedures.remove(purchaseDetail); }

    public void SumTotal() {
        if (procedures != null) {
            float sum = 0;
            for (PurchaseDetail d : procedures) {
                sum += d.getSubtotal();
            }
            this.total = sum;
        }
    }
}
