package com.example.testareonline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    @GetMapping("/")
    public String getIndexPage(){
        return "index";
    }
    @GetMapping("/page/home")
    public String getHomePage(){
        return "home";
    }

    @GetMapping("/page/register")
    public String getRegisterPage(){
        return "register";
    }

    @GetMapping("/page/create_quiz")
    public String getCreateQuizPage(){
        return "create_quiz";
    }

    @GetMapping("/page/quiz")
    public String getQuizPage(@RequestParam long idQuiz){
        return "quiz";
    }

    @GetMapping("/page/quiz/rezultate/{idQuiz}")
    public String getRezultateQuizPage(@PathVariable long idQuiz){
        return "rezultate_quiz";
    }

    @GetMapping("/page/quiz/raspunsuri")
    public String getRaspunsuriParticipantPage(@RequestParam long idQuiz, @RequestParam long idUser){
        return "raspunsuri_quiz_participant";
    }

}
