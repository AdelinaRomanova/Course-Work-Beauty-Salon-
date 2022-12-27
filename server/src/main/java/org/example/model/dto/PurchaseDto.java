package org.example.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.model.Purchase;

@Data
public class PurchaseDto {
    private Long id;
    private LocalDateTime date;
    private float total;
    private List<PurchaseDetailDto> procedures = new ArrayList<>();

    public static PurchaseDto from(Purchase purchase) {
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setId(purchase.getId());
        purchaseDto.setDate(purchase.getDate());
        purchaseDto.setTotal(purchase.getTotal());
        purchaseDto.setProcedures(purchase.getProcedures().stream().map(PurchaseDetailDto::from).collect(Collectors.toList()));
        return purchaseDto;
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

    public List<PurchaseDetailDto> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<PurchaseDetailDto> procedures) {
        this.procedures = procedures;
    }

    public void addProcedure(PurchaseDetailDto purchaseDetailDto) { procedures.add(purchaseDetailDto); }
}