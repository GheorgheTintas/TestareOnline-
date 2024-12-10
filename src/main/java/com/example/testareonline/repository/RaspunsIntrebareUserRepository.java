package com.example.testareonline.repository;
import com.example.testareonline.model.RaspunsIntrebareUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaspunsIntrebareUserRepository extends CrudRepository<RaspunsIntrebareUser, Long> {
    @Query("SELECT r FROM RaspunsIntrebareUser r "
            +            "JOIN Intrebare i ON r.idIntrebare = i.id "
            +            "WHERE r.idUser = :idUser AND i.idQuiz = :idQuiz")
    List<RaspunsIntrebareUser> findByIdUserAndIdQuiz(@Param("idUser") Long idUser, @Param("idQuiz") Long idQuiz);
}
