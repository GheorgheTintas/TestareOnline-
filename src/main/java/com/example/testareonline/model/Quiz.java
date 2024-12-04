package com.example.testareonline.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Quiz {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @javax.persistence.Column(name = "id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "id_user_owner")
    private long idUserOwner;

    public long getIdUserOwner() {
        return idUserOwner;
    }

    public void setIdUserOwner(long idUserOwner) {
        this.idUserOwner = idUserOwner;
    }

    @Basic
    @Column(name = "nume")
    private String nume;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Basic
    @Column(name = "data_creare")
    private Timestamp dataCreare;

    public Timestamp getDataCreare() {
        return dataCreare;
    }

    public void setDataCreare(Timestamp dataCreare) {
        this.dataCreare = dataCreare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz quiz = (Quiz) o;

        if (id != quiz.id) return false;
        if (idUserOwner != quiz.idUserOwner) return false;
        if (nume != null ? !nume.equals(quiz.nume) : quiz.nume != null) return false;
        if (dataCreare != null ? !dataCreare.equals(quiz.dataCreare) : quiz.dataCreare != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (idUserOwner ^ (idUserOwner >>> 32));
        result = 31 * result + (nume != null ? nume.hashCode() : 0);
        result = 31 * result + (dataCreare != null ? dataCreare.hashCode() : 0);
        return result;
    }
}
