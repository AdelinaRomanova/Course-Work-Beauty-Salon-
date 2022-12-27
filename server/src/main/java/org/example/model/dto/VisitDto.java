package org.example.model.dto;

import lombok.Data;
import org.example.model.Visit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VisitDto {
    private Long id;
    private LocalDateTime date;
    private List<VisitDetailDto> procedures = new ArrayList<>();

    public static VisitDto from(Visit visit) {
        VisitDto visitDto = new VisitDto();
        visitDto.setDate(visit.getDate());
        visitDto.setId(visit.getId());
        visitDto.setProcedures(visit.getProcedures().stream().map(VisitDetailDto::from).collect(Collectors.toList()));
        return visitDto;
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

    public List<VisitDetailDto> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<VisitDetailDto> procedures) {
        this.procedures = procedures;
    }
}
