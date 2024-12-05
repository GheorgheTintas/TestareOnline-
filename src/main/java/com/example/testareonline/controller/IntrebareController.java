package com.example.testareonline.controller;

import com.example.testareonline.model.Intrebare;
import com.example.testareonline.model.Quiz;
import com.example.testareonline.repository.IntrebareRepository;
import com.example.testareonline.repository.QuizRepository;
import com.example.testareonline.repository.UserQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/intrebare")

public class IntrebareController {

    @Autowired

    private IntrebareRepository intrebareRepository;

    @Autowired

    private UserQuizRepository userQuizRepository;

    @Autowired

    private QuizRepository quizRepository;

    @GetMapping("/get/intrebari/{quizId}")
    public ResponseEntity<?> getIntrebari(@PathVariable long quizId, HttpServletRequest request) {
        // Retrieve the logged-in user's ID from cookies
        Optional<Cookie> userCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "userId".equals(cookie.getName()))
                .findFirst();

        if (userCookie.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: User not logged in.");
        }
        long loggedInUserId;
        try {
            loggedInUserId = Long.parseLong(userCookie.get().getValue());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid user ID in cookie.");
        }

        // Check if the user is joined in the quiz
        boolean isUserJoined = userQuizRepository.existsByIdQuizAndIdUserParticipant(quizId, loggedInUserId);
        if (!isUserJoined) {
            return ResponseEntity.status(403).body("Forbidden: User not joined in the quiz.");
        }
        // Fetch and hash questions
        List<Intrebare> intrebariOriginale = intrebareRepository.findByIdQuiz(quizId);
        List<Intrebare> intrebariClone = new ArrayList<>();

        for (Intrebare intrebare : intrebariOriginale) {
            intrebariClone.add(new Intrebare(intrebare)); // presupune că ai un constructor copy
        }

        List<Intrebare> hashedIntrebari = intrebariClone.stream().map(intrebare -> {
            intrebare.setVariantaRaspunsCorecta(DigestUtils.md5DigestAsHex(intrebare.getVariantaRaspunsCorecta().getBytes()));
            return intrebare;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(hashedIntrebari);
    }
    // Endpoint with unaltered answers
    @GetMapping("/get/intrebari_cu_raspunsuri/{quizId}")
    public ResponseEntity<?> getIntrebariWithAnswers(@PathVariable long quizId, HttpServletRequest request) {
        Optional<Cookie> userCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "userId".equals(cookie.getName()))
                .findFirst();

        if (userCookie.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: User not logged in.");
        }

        long loggedInUserId;
        try {
            loggedInUserId = Long.parseLong(userCookie.get().getValue());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid user ID in cookie.");
        }

        boolean isUserJoined = userQuizRepository.existsByIdQuizAndIdUserParticipant(quizId, loggedInUserId);
        if (!isUserJoined) {
            return ResponseEntity.status(403).body("Forbidden: User not joined in the quiz.");
        }

        List<Intrebare> intrebari = intrebareRepository.findByIdQuiz(quizId);

        return ResponseEntity.ok(intrebari);
    }

    @PostMapping("/adauga")
    public ResponseEntity<Object> addIntrebare(HttpServletRequest request, @RequestBody Intrebare intrebare) {
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
        Optional<Quiz> quizOptional = quizRepository.findById(intrebare.getIdQuiz());
        if (quizOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Quiz not found.");
        }
        Quiz quiz = quizOptional.get();
        // Only the owner of the quiz can add questions
        if (quiz.getIdUserOwner() != idUserOwner) {
            return ResponseEntity.status(403).body("Access denied: Only the owner can add questions.");
        }
        Intrebare intrebareSalvata =intrebareRepository.save(intrebare);
        return ResponseEntity.ok(intrebareSalvata);
    }
    @DeleteMapping("/sterge")
    public ResponseEntity<String> deleteIntrebare(HttpServletRequest request, @RequestParam long idQuiz, @RequestParam long idIntrebare) {
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

        // Only the owner of the quiz can delete questions
        if (quiz.getIdUserOwner() != idUserOwner) {
            return ResponseEntity.status(403).body("Access denied: Only the owner can delete questions.");
        }

        // Check if the question exists in the quiz
        Optional<Intrebare> intrebareOptional = intrebareRepository.findById(idIntrebare);

        if (intrebareOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Question not found.");
        }

        Intrebare intrebare = intrebareOptional.get();

        // Check if the question belongs to the specified quiz
        if (intrebare.getIdQuiz() != idQuiz) {
            return ResponseEntity.status(400).body("This question does not belong to the specified quiz.");
        }

        intrebareRepository.delete(intrebare);
        return ResponseEntity.ok("Question successfully deleted from the quiz.");
    }



}

