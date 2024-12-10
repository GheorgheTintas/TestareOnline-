package com.example.testareonline.controller;

import com.example.testareonline.model.Intrebare;
import com.example.testareonline.model.Quiz;
import com.example.testareonline.model.RaspunsIntrebareUser;
import com.example.testareonline.repository.IntrebareRepository;
import com.example.testareonline.repository.QuizRepository;
import com.example.testareonline.repository.RaspunsIntrebareUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping(value="/raspunsuri_intrebari")
public class RaspunsIntrebareUserController {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private RaspunsIntrebareUserRepository raspunsIntrebareUserRepository;
    @Autowired
    private IntrebareRepository intrebareRepository;
    @GetMapping("/get") //returneaza ce raspunsuri a dat un utilizator la un quiz
    public ResponseEntity<List<RaspunsIntrebareUser>> getRaspunsuriForUser(
            HttpServletRequest request,
            @RequestParam long idQuiz) {
        // Get the logged in user from the cookie
        Optional<Cookie> userCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "userId".equals(cookie.getName()))
                .findFirst();

        if (userCookie.isEmpty()) {
            return ResponseEntity.status(403).body(null); // User is not authenticated
        }

        long idUser;
        try {
            idUser = Long.parseLong(userCookie.get().getValue());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null); // Invalid user ID in cookie
        }

        // Check if the quiz exists
        Optional<Quiz> quizOptional = quizRepository.findById(idQuiz);
        if (quizOptional.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Quiz not found
        }

        // Retrieve all answers given by the user for the questions in the specified quiz
        List<RaspunsIntrebareUser> raspunsuri = raspunsIntrebareUserRepository.findByIdUserAndIdQuiz(idUser, idQuiz);

        if (raspunsuri.isEmpty()) {
            return ResponseEntity.status(404).body(null); // No answers found for the user in this quiz
        }

        return ResponseEntity.ok(raspunsuri); // Return the list of answers
    }

    @PostMapping("/trimite")

    public ResponseEntity<Object> submitAnswersForQuiz(

            HttpServletRequest request,

            @RequestParam long idQuiz,

            @RequestBody List<RaspunsIntrebareUser> raspunsuri) {

        // Get the logged in user from the cookie

        Optional<Cookie> userCookie = Arrays.stream(request.getCookies())

                .filter(cookie -> "userId".equals(cookie.getName()))

                .findFirst();

        if (userCookie.isEmpty()) {

            return ResponseEntity.status(403).body("User not authenticated");

        }

        long idUser;

        try {

            idUser = Long.parseLong(userCookie.get().getValue());

        } catch (NumberFormatException e) {

            return ResponseEntity.badRequest().body("Invalid user ID in cookie");

        }

        // Check if the quiz exists

        Optional<Quiz> quizOptional = quizRepository.findById(idQuiz);

        if (quizOptional.isEmpty()) {

            return ResponseEntity.status(404).body("Quiz not found");

        }

        // Validate if the answers are for the correct quiz

        for (RaspunsIntrebareUser raspuns : raspunsuri) {

            if (raspuns.getIdIntrebare() == 0) {

                return ResponseEntity.badRequest().body("Invalid question ID in answers");

            }

            // Ensure the answer belongs to the correct quiz

            Optional<Intrebare> intrebareOptional = intrebareRepository.findById(raspuns.getIdIntrebare());

            if (intrebareOptional.isEmpty() || intrebareOptional.get().getIdQuiz() != idQuiz) {

                return ResponseEntity.badRequest().body("Answer does not belong to the specified quiz");

            }

        }

        // Save the answers for the user
        List<RaspunsIntrebareUser> listaRaspunsuri = new ArrayList<>();
        for (RaspunsIntrebareUser raspuns : raspunsuri) {

            raspuns.setIdUser(idUser); // Set the logged in user ID
            RaspunsIntrebareUser raspunsIntrebareUserSalvat = raspunsIntrebareUserRepository.save(raspuns);// Save the answer
            listaRaspunsuri.add(raspunsIntrebareUserSalvat);
        }
        return ResponseEntity.ok(listaRaspunsuri);
    }


}
