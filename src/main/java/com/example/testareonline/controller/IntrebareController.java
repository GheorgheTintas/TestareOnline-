package com.example.testareonline.controller;

import com.example.testareonline.model.Intrebare;
import com.example.testareonline.model.Quiz;
import com.example.testareonline.repository.IntrebareRepository;
import com.example.testareonline.repository.QuizRepository;
import com.example.testareonline.repository.UserQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/get/intrebari/{idQuiz}")
    public ResponseEntity<?> getIntrebari(HttpServletRequest request, @PathVariable long idQuiz) {
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
        boolean isUserJoined = userQuizRepository.existsByIdQuizAndIdUserParticipant(idQuiz, loggedInUserId);
        if (!isUserJoined) {
            return ResponseEntity.status(403).body("Forbidden: User not joined in the quiz.");
        }
        // Fetch and hash questions
        List<Intrebare> intrebariOriginale = intrebareRepository.findByIdQuiz(idQuiz);
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
    @GetMapping("/get/intrebari_cu_raspunsuri/{idQuiz}")
    public ResponseEntity<?> getIntrebariWithAnswers(@PathVariable long idQuiz, HttpServletRequest request) {
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

        boolean isUserJoined = userQuizRepository.existsByIdQuizAndIdUserParticipant(idQuiz, loggedInUserId);
        if (!isUserJoined) {
            return ResponseEntity.status(403).body("Forbidden: User not joined in the quiz.");
        }

        List<Intrebare> intrebari = intrebareRepository.findByIdQuiz(idQuiz);

        return ResponseEntity.ok(intrebari);
    }
    @PostMapping("/adauga")
    public ResponseEntity<Object> addIntrebari(HttpServletRequest request, @RequestBody List<Intrebare> intrebari) {
        // Check for user authentication
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

        // Validate questions list
        if (intrebari == null || intrebari.isEmpty()) {
            return ResponseEntity.badRequest().body("No questions provided.");
        }

        // Validate the quiz and ownership for all questions
        long quizId = intrebari.get(0).getIdQuiz(); // Assume all questions belong to the same quiz
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (quizOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Quiz not found.");
        }

        Quiz quiz = quizOptional.get();
        if (quiz.getIdUserOwner() != idUserOwner) {
            return ResponseEntity.status(403).body("Access denied: Only the owner can add questions.");
        }

        // Save all questions in the database
        for (Intrebare intrebare : intrebari) {
            if (intrebare.getIdQuiz() != quizId) {
                return ResponseEntity.badRequest().body("All questions must belong to the same quiz.");
            }
        }
        List<Intrebare> savedQuestions = (List<Intrebare>) intrebareRepository.saveAll(intrebari);

        return ResponseEntity.ok(savedQuestions);
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

