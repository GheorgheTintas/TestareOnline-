package com.example.testareonline.repository;

import com.example.testareonline.model.Quiz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface QuizRepository extends CrudRepository<Quiz, Long> {
    List<Quiz> findByIdUserOwner(Long idUserOwner);
}


