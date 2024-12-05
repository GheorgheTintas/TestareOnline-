package com.example.testareonline.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class UserQuizPK implements Serializable {
    @Column(name = "id_quiz")
    @Id
    private long idQuiz;
    @Column(name = "id_user_participant")
    @Id
    private long idUserParticipant;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserQuizPK that = (UserQuizPK) o;

        if (idQuiz != that.idQuiz) return false;
        if (idUserParticipant != that.idUserParticipant) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (idQuiz ^ (idQuiz >>> 32));
        result = 31 * result + (int) (idUserParticipant ^ (idUserParticipant >>> 32));
        return result;
    }
}
