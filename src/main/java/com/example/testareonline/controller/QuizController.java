package com.example.testareonline.controller;

import com.example.testareonline.model.ParticipantDTO;
import com.example.testareonline.model.Quiz;
import com.example.testareonline.model.User;
import com.example.testareonline.model.UserQuiz;
import com.example.testareonline.repository.QuizRepository;
import com.example.testareonline.repository.UserQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping(value = "/quiz")

public class QuizController {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserQuizRepository userQuizRepository;

    @GetMapping(value="/owner")
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
    @PostMapping("/adauga")
    public ResponseEntity<Object> adaugaQuiz(@RequestBody Quiz quiz, HttpServletRequest request) {
        // Retrieve "idUserOwner" from cookies
        Optional<Cookie> userCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "userId".equals(cookie.getName()))
                .findFirst();

        if (userCookie.isPresent()) {
            try {
                long idUserOwner = Long.parseLong(userCookie.get().getValue());
                quiz.setIdUserOwner(idUserOwner);
                quiz.setDataCreare(new Timestamp(System.currentTimeMillis()));

                // Save the quiz to the database
                Quiz quizSalvat = quizRepository.save(quiz);
                // Add the quiz owner to the user_quiz table
                UserQuiz userQuiz = new UserQuiz();
                userQuiz.setIdQuiz(quizSalvat.getId());
                userQuiz.setIdUserParticipant(idUserOwner);
                userQuiz.setPunctaj(-1); // No score initially
                userQuizRepository.save(userQuiz);

                return new ResponseEntity<>(quizSalvat, HttpStatus.OK);

            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid idUserOwner value in cookie.");
            }
        } else {
            return ResponseEntity.badRequest().body("Cookie idUserOwner not found.");
        }

    }

    @DeleteMapping("/sterge/{id}")
    public ResponseEntity<String> stergeQuiz(@PathVariable long id, HttpServletRequest request) {

        Optional<Cookie> userCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "userId".equals(cookie.getName()))
                .findFirst();

        if (userCookie.isEmpty()) {
            return ResponseEntity.status(403).body("Access denied: User not authenticated.");
        }

        long idUser;
        try {
            idUser = Long.parseLong(userCookie.get().getValue());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid idUser in cookie.");
        }

        // Find the quiz by ID
        Optional<Quiz> quizOptional = quizRepository.findById(id);

        if (quizOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Quiz not found.");
        }

        Quiz quiz = quizOptional.get();

        // Check if the user is the owner
        if (quiz.getIdUserOwner() != idUser) {
            return ResponseEntity.status(403).body("Access denied: Only the owner can delete this quiz.");
        }

        // Delete the quiz
        quizRepository.deleteById(id);
        return ResponseEntity.ok("Quiz deleted successfully!");
    }

    @GetMapping("/participanti/{idQuiz}")
    public ResponseEntity<List<ParticipantDTO>> getParticipanti(@PathVariable long idQuiz) {
        List<ParticipantDTO> participanti = userQuizRepository.findParticipantsByQuizId(idQuiz);
        return ResponseEntity.ok(participanti);
    }

}
