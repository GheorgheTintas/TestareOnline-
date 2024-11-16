package com.example.testareonline.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.testareonline.model.UsersEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UsersEntity, Long> {
    Optional<UsersEntity> findByUsername(String loginUserName);}