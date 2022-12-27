package org.example.model.dto;

import lombok.Data;

import org.example.model.VisitDetail;

@Data
public class VisitDetailDto {
    private Long id;
    private String procedureName;

    public static VisitDetailDto from(VisitDetail vd) {
        VisitDetailDto rdd = new VisitDetailDto();
        rdd.setId(vd.getProcedure().getId());
        rdd.setProcedureName(vd.getProcedure().getName());
        return rdd;
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
}
