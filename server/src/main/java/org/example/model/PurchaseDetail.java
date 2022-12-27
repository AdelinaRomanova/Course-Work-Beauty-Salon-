package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.model.dto.PurchaseDetailDto;

@Data
@Entity
@Table(name = "purchase_details")
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @ManyToOne(fetch = FetchType.EAGER, cascade =  CascadeType.PERSIST)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    private int count;
    private float subtotal;

    public PurchaseDetail() { }

    public static PurchaseDetail from(PurchaseDetailDto rdd) {
        PurchaseDetail rd = new PurchaseDetail();
        rd.setCount(rdd.getCount());
        rd.setSubtotal(rdd.getSubtotal());
        return rd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }
}
