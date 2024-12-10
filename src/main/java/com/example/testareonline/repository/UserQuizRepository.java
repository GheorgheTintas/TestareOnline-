package com.example.testareonline.repository;

import com.example.testareonline.model.UserQuiz;
import com.example.testareonline.model.UserQuizPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuizRepository extends CrudRepository<UserQuiz, UserQuizPK> {
    boolean existsByIdQuizAndIdUserParticipant(long quizId, long idUserParticipant);
    boolean existsById(UserQuizPK userQuizPK);
    void deleteById(UserQuizPK userQuizPK);

}