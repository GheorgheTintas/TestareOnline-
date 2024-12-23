package com.example.testareonline.repository;
import com.example.testareonline.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String loginUserName);
    boolean existsByUsername(String username);
}