package com.example.testareonline.controller;

import com.example.testareonline.model.Quiz;
import com.example.testareonline.model.UserQuiz;
import com.example.testareonline.model.UserQuizPK;
import com.example.testareonline.repository.QuizRepository;
import com.example.testareonline.repository.UserQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping(value="/user_quiz")
public class UserQuizController {
    @Autowired
    private UserQuizRepository userQuizRepository;
    @Autowired
    private QuizRepository quizRepository;

    @PostMapping("/adauga/{idQuiz}")
    public ResponseEntity<Object> joinQuiz(HttpServletRequest request, @PathVariable long idQuiz) {
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
            return ResponseEntity.badRequest().body("Invalid user ID in cookie.");
        }

        // Check if the user is already joined to the quiz
        UserQuizPK userQuizPK = new UserQuizPK();
        userQuizPK.setIdQuiz(idQuiz);
        userQuizPK.setIdUserParticipant(idUser);

        if (userQuizRepository.existsById(userQuizPK)) {
            return ResponseEntity.status(400).body("User is already joined to this quiz.");
        }

        UserQuiz userQuiz = new UserQuiz();
        userQuiz.setIdQuiz(idQuiz);
        userQuiz.setIdUserParticipant(idUser);
        userQuiz.setPunctaj(-1);

        UserQuiz userQuizSalvat = userQuizRepository.save(userQuiz);
        return ResponseEntity.ok(userQuizSalvat);
    }

    @DeleteMapping("/sterge/{idQuiz}/{idUserToRemove}")
    public ResponseEntity<String> removeUserFromQuiz(HttpServletRequest request, @PathVariable long idQuiz,
                                                     @PathVariable long idUserToRemove) {
        Optional<Cookie> userCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "userId".equals(cookie.getName()))
                .findFirst();

        if (userCookie.isEmpty()) {
            return ResponseEntity.status(403).body("Access denied: User not authenticated.");
        }

        long idUserOwner;
        try {
            idUserOwner = Long.parseLong(userCookie.get().getValue());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid user ID in cookie.");
        }

        // Check if the quiz exists and retrieve the owner of the quiz
        Optional<Quiz> quizOptional = quizRepository.findById(idQuiz);

        if (quizOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Quiz not found.");
        }

        Quiz quiz = quizOptional.get();
        // Only the owner of the quiz can remove users
        if (quiz.getIdUserOwner() != idUserOwner) {
            return ResponseEntity.status(403).body("Access denied: Only the owner can remove users.");
        }
        // Ensure the owner cannot remove themselves
        if (idUserOwner == idUserToRemove) {
            return ResponseEntity.status(400).body("Owner cannot remove themselves from the quiz.");
        }
        // Remove user from the quiz
        UserQuizPK userQuizPK = new UserQuizPK();
        userQuizPK.setIdQuiz(idQuiz);
        userQuizPK.setIdUserParticipant(idUserToRemove);

        if (!userQuizRepository.existsById(userQuizPK)) {
            return ResponseEntity.status(404).body("User is not part of this quiz.");
        }
        userQuizRepository.deleteById(userQuizPK);
        return ResponseEntity.ok("User successfully removed from the quiz.");
    }
}
