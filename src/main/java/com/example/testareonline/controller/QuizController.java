package com.example.testareonline.controller;

import com.example.testareonline.repository.QuizRepository;
import com.example.testareonline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController

@RequestMapping(value = "/quiz")

public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    @GetMapping(value="/get")
    public ResponseEntity<Object> getQuizesOfUser(HttpServletRequest request){
        Long userId = (long) -1;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("userId".equals(cookie.getName())) {
                userId = Long.valueOf(cookie.getValue());
            }
        }
        return new ResponseEntity<>(quizRepository.findByIdUserOwner(userId), HttpStatus.OK);
    }

}
