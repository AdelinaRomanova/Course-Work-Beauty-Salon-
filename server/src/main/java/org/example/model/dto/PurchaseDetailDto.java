package org.example.model.dto;

import lombok.Data;

import org.example.model.PurchaseDetail;

@Data
public class PurchaseDetailDto {
    private Long id;
    private String procedureName;
    private int count;
    private float subtotal;

    public static PurchaseDetailDto from(PurchaseDetail pd) {
        PurchaseDetailDto pdd = new PurchaseDetailDto();
        pdd.setId(pd.getProcedure().getId());
        pdd.setProcedureName(pd.getProcedure().getName());
        pdd.setCount(pd.getCount());
        pdd.setSubtotal(pd.getProcedure().getPrice()*pd.getCount());
        return pdd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
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
