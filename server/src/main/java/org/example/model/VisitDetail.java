package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.model.dto.VisitDetailDto;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name = "visit_details")
public class VisitDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "procedure_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Procedure procedure;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    public VisitDetail() {}

    public static VisitDetail from(VisitDetailDto vdd) {
        VisitDetail dd = new VisitDetail();
        dd.setId(vdd.getId());
        return dd;
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

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }
}