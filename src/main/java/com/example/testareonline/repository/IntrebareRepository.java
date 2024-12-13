package com.example.testareonline.repository;

import com.example.testareonline.model.Intrebare;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IntrebareRepository extends CrudRepository<Intrebare, Long> {
    List<Intrebare> findByIdQuiz(long idQuiz);
}