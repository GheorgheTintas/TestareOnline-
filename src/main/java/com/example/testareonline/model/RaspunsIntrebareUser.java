package com.example.testareonline.model;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "raspuns_intrebare_user", schema = "testareonline", catalog = "")
@IdClass(com.example.testareonline.model.RaspunsIntrebareUserPK.class)
public class RaspunsIntrebareUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @javax.persistence.Column(name = "id_user")
    private long idUser;

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @javax.persistence.Column(name = "id_intrebare")
    private long idIntrebare;

    public long getIdIntrebare() {
        return idIntrebare;
    }

    public void setIdIntrebare(long idIntrebare) {
        this.idIntrebare = idIntrebare;
    }

    @Basic
    @Column(name = "raspuns_dat")
    private String raspunsDat;

    public String getRaspunsDat() {
        return raspunsDat;
    }

    public void setRaspunsDat(String raspunsDat) {
        this.raspunsDat = raspunsDat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaspunsIntrebareUser that = (RaspunsIntrebareUser) o;

        if (idUser != that.idUser) return false;
        if (idIntrebare != that.idIntrebare) return false;
        if (raspunsDat != null ? !raspunsDat.equals(that.raspunsDat) : that.raspunsDat != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (idUser ^ (idUser >>> 32));
        result = 31 * result + (int) (idIntrebare ^ (idIntrebare >>> 32));
        result = 31 * result + (raspunsDat != null ? raspunsDat.hashCode() : 0);
        return result;
    }
}
