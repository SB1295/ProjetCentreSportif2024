package be.atc.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "closes")
public class Close {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "close_id", nullable = false)
    private int id;

    @Column(name = "begin_date", nullable = false)
    private Instant beginDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_sport_field_id", nullable = false)
    private SportField fkSportField;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instant getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Instant beginDate) {
        this.beginDate = beginDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public SportField getFkSportField() {
        return fkSportField;
    }

    public void setFkSportField(SportField fkSportField) {
        this.fkSportField = fkSportField;
    }

}