package com.example.testareonline.model;

import javax.persistence.*;

@Entity
@Table(name = "user_quiz", schema = "testareonline", catalog = "")
@IdClass(UserQuizPK.class)
public class UserQuiz {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_quiz")
    private long idQuiz;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_user_participant")
    private long idUserParticipant;
    @Basic
    @Column(name = "punctaj")
    private Integer punctaj;

    public long getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(long idQuiz) {
        this.idQuiz = idQuiz;
    }

    public long getIdUserParticipant() {
        return idUserParticipant;
    }

    public void setIdUserParticipant(long idUserParticipant) {
        this.idUserParticipant = idUserParticipant;
    }

    public Integer getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(Integer punctaj) {
        this.punctaj = punctaj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserQuiz userQuiz = (UserQuiz) o;

        if (idQuiz != userQuiz.idQuiz) return false;
        if (idUserParticipant != userQuiz.idUserParticipant) return false;
        if (punctaj != null ? !punctaj.equals(userQuiz.punctaj) : userQuiz.punctaj != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (idQuiz ^ (idQuiz >>> 32));
        result = 31 * result + (int) (idUserParticipant ^ (idUserParticipant >>> 32));
        result = 31 * result + (punctaj != null ? punctaj.hashCode() : 0);
        return result;
    }
}
