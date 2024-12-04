package com.example.testareonline.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class RaspunsIntrebareUserPK implements Serializable {
    @Column(name = "id_user")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;
    @Column(name = "id_intrebare")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idIntrebare;

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdIntrebare() {
        return idIntrebare;
    }

    public void setIdIntrebare(long idIntrebare) {
        this.idIntrebare = idIntrebare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaspunsIntrebareUserPK that = (RaspunsIntrebareUserPK) o;

        if (idUser != that.idUser) return false;
        if (idIntrebare != that.idIntrebare) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (idUser ^ (idUser >>> 32));
        result = 31 * result + (int) (idIntrebare ^ (idIntrebare >>> 32));
        return result;
    }
}
