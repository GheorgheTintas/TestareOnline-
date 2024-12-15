package com.example.testareonline.repository;

import com.example.testareonline.dto.ParticipantDTO;
import com.example.testareonline.model.UserQuiz;
import com.example.testareonline.model.UserQuizPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuizRepository extends CrudRepository<UserQuiz, UserQuizPK> {
    boolean existsByIdQuizAndIdUserParticipant(long idQuiz, long idUserParticipant);
    boolean existsById(UserQuizPK userQuizPK);
    void deleteById(UserQuizPK userQuizPK);
    UserQuiz findByIdQuizAndIdUserParticipant(long idQuiz, long idUserParticipant);

    // Query to fetch participants with their ids, usernames, and scores
    @Query("SELECT new com.example.testareonline.dto.ParticipantDTO(uq.idUserParticipant, u.username, uq.punctaj) " +
            "FROM UserQuiz uq " +
            "JOIN uq.user u " +
            "WHERE uq.idQuiz = :idQuiz")
    List<ParticipantDTO> findParticipantsByQuizId(@Param("idQuiz") long idQuiz);
}