package org.example.model.dto;

import lombok.Data;

import org.example.model.Procedure;
@Data
public class ProcedureDto {
    private Long id;
    private String name;
    private String master;
    private float price;

    public static ProcedureDto from(Procedure procedure) {
        ProcedureDto procedureDto = new ProcedureDto();
        procedureDto.setId(procedure.getId());
        procedureDto.setName(procedure.getName());
        procedureDto.setMaster(procedure.getMaster());
        procedureDto.setPrice(procedure.getPrice());
        return procedureDto;
    }

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
}
