package com.example.testareonline.model;

import javax.persistence.*;

@Entity
public class Intrebare {
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
    @Column(name = "id_quiz")
    private long idQuiz;

    public long getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(long idQuiz) {
        this.idQuiz = idQuiz;
    }


    @Basic
    @Column(name = "descriere_intrebare")
    private String descriereIntrebare;

    public String getDescriereIntrebare() {
        return descriereIntrebare;
    }

    public void setDescriereIntrebare(String descriereIntrebare) {
        this.descriereIntrebare = descriereIntrebare;
    }

    @Basic
    @Column(name = "varianta_raspuns_1")
    private String variantaRaspuns1;

    public String getVariantaRaspuns1() {
        return variantaRaspuns1;
    }

    public void setVariantaRaspuns1(String variantaRaspuns1) {
        this.variantaRaspuns1 = variantaRaspuns1;
    }

    @Basic
    @Column(name = "varianta_raspuns_2")
    private String variantaRaspuns2;

    public String getVariantaRaspuns2() {
        return variantaRaspuns2;
    }

    public void setVariantaRaspuns2(String variantaRaspuns2) {
        this.variantaRaspuns2 = variantaRaspuns2;
    }

    @Basic
    @Column(name = "varianta_raspuns_3")
    private String variantaRaspuns3;

    public String getVariantaRaspuns3() {
        return variantaRaspuns3;
    }

    public void setVariantaRaspuns3(String variantaRaspuns3) {
        this.variantaRaspuns3 = variantaRaspuns3;
    }

    @Basic
    @Column(name = "varianta_raspuns_4")
    private String variantaRaspuns4;

    public String getVariantaRaspuns4() {
        return variantaRaspuns4;
    }

    public void setVariantaRaspuns4(String variantaRaspuns4) {
        this.variantaRaspuns4 = variantaRaspuns4;
    }

    @Basic
    @Column(name = "varianta_raspuns_corecta")
    private String variantaRaspunsCorecta;

    public String getVariantaRaspunsCorecta() {
        return variantaRaspunsCorecta;
    }

    public void setVariantaRaspunsCorecta(String variantaRaspunsCorecta) {
        this.variantaRaspunsCorecta = variantaRaspunsCorecta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Intrebare intrebare = (Intrebare) o;

        if (id != intrebare.id) return false;
        if (idQuiz != intrebare.idQuiz) return false;
        if (descriereIntrebare != null ? !descriereIntrebare.equals(intrebare.descriereIntrebare) : intrebare.descriereIntrebare != null)
            return false;
        if (variantaRaspuns1 != null ? !variantaRaspuns1.equals(intrebare.variantaRaspuns1) : intrebare.variantaRaspuns1 != null)
            return false;
        if (variantaRaspuns2 != null ? !variantaRaspuns2.equals(intrebare.variantaRaspuns2) : intrebare.variantaRaspuns2 != null)
            return false;
        if (variantaRaspuns3 != null ? !variantaRaspuns3.equals(intrebare.variantaRaspuns3) : intrebare.variantaRaspuns3 != null)
            return false;
        if (variantaRaspuns4 != null ? !variantaRaspuns4.equals(intrebare.variantaRaspuns4) : intrebare.variantaRaspuns4 != null)
            return false;
        if (variantaRaspunsCorecta != null ? !variantaRaspunsCorecta.equals(intrebare.variantaRaspunsCorecta) : intrebare.variantaRaspunsCorecta != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (idQuiz ^ (idQuiz >>> 32));
        result = 31 * result + (descriereIntrebare != null ? descriereIntrebare.hashCode() : 0);
        result = 31 * result + (variantaRaspuns1 != null ? variantaRaspuns1.hashCode() : 0);
        result = 31 * result + (variantaRaspuns2 != null ? variantaRaspuns2.hashCode() : 0);
        result = 31 * result + (variantaRaspuns3 != null ? variantaRaspuns3.hashCode() : 0);
        result = 31 * result + (variantaRaspuns4 != null ? variantaRaspuns4.hashCode() : 0);
        result = 31 * result + (variantaRaspunsCorecta != null ? variantaRaspunsCorecta.hashCode() : 0);
        return result;
    }
    public Intrebare(){}
    public Intrebare(Intrebare original) {
        this.id = original.id;
        this.idQuiz = original.idQuiz;
        this.descriereIntrebare = original.descriereIntrebare;
        this.variantaRaspuns1 = original.variantaRaspuns1;// Referință la aceeași listă
        this.variantaRaspuns2 = original.variantaRaspuns2;
        this.variantaRaspuns3 = original.variantaRaspuns3;
        this.variantaRaspuns4 = original.variantaRaspuns4;
        this.variantaRaspunsCorecta = original.variantaRaspunsCorecta;
    }

}
