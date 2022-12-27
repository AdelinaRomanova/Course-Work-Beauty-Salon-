package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import org.example.model.dto.VisitDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "visits")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "visit", cascade = CascadeType.ALL)
    private List<VisitDetail> procedures = new ArrayList<>();

    public Visit () { }

    public static Visit from(VisitDto visitDto) {
        Visit visit = new Visit();
        visit.setDate(visitDto.getDate());
        visit.setProcedures(visitDto.getProcedures().stream().map(VisitDetail::from).collect(Collectors.toList()));
        return visit;
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

    public List<VisitDetail> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<VisitDetail> procedures) {
        this.procedures = procedures;
    }

    public void addProcedureVisit(VisitDetail visitDetail) {
        procedures.add(visitDetail);
    }

    public void removeProcedureVisit(VisitDetail visitDetail) { procedures.remove(visitDetail); }
}
