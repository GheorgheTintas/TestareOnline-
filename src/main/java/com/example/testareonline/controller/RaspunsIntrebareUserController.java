package com.example.testareonline.controller;

import com.example.testareonline.model.Intrebare;
import com.example.testareonline.model.Quiz;
import com.example.testareonline.model.RaspunsIntrebareUser;
import com.example.testareonline.model.UserQuiz;
import com.example.testareonline.repository.IntrebareRepository;
import com.example.testareonline.repository.QuizRepository;
import com.example.testareonline.repository.RaspunsIntrebareUserRepository;
import com.example.testareonline.repository.UserQuizRepository;
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
@RequestMapping(value = "/raspunsuri_intrebari")
public class RaspunsIntrebareUserController {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private RaspunsIntrebareUserRepository raspunsIntrebareUserRepository;
    @Autowired
    private IntrebareRepository intrebareRepository;
    @Autowired
    private UserQuizRepository userQuizRepository;

    @GetMapping("/get/{idQuiz}/{idParticipant}") // Return answers submitted by a participant for a specific quiz
    public ResponseEntity<List<RaspunsIntrebareUser>> getRaspunsuriParticipant(
            HttpServletRequest request,
            @PathVariable long idQuiz,
            @PathVariable long idParticipant) {
        // Get the logged-in user from the cookie
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

        Quiz quiz = quizOptional.get();

        // Check if the logged-in user is the owner of the quiz
        if (quiz.getIdUserOwner() != idUser) {
            return ResponseEntity.status(403).body(null); // Forbidden: User is not the owner of the quiz
        }

        // Retrieve answers given by the specified participant for the specified quiz
        List<RaspunsIntrebareUser> raspunsuri = raspunsIntrebareUserRepository.findByIdUserAndIdQuiz(idParticipant, idQuiz);

        if (raspunsuri.isEmpty()) {
            return ResponseEntity.status(404).body(null); // No answers found for this participant in this quiz
        }

        return ResponseEntity.ok(raspunsuri); // Return the list of answers
    }

    @PostMapping("/trimite/{idQuiz}")
    public ResponseEntity<Object> salveazaRaspunsuri(
            HttpServletRequest request,
            @PathVariable long idQuiz,
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

        int punctaj = calculeazaPunctaj(idUser, idQuiz);

        return ResponseEntity.ok(punctaj);
    }


    private int calculeazaPunctaj(long idUser, long idQuiz) {
        try {
            // Fetch all questions for the quiz
            List<Intrebare> intrebari = intrebareRepository.findByIdQuiz(idQuiz);

            // Fetch participant's answers for the quiz
            List<RaspunsIntrebareUser> raspunsuri = raspunsIntrebareUserRepository.findByIdUserAndIdQuiz(idUser, idQuiz);

            // Calculate the score
            int punctaj = 0;
            for (Intrebare intrebare : intrebari) {
                for (RaspunsIntrebareUser raspuns : raspunsuri) {
                    if (intrebare.getId() == raspuns.getIdIntrebare() &&
                            intrebare.getVariantaRaspunsCorecta().equals(raspuns.getRaspunsDat())) {
                        punctaj += 1; // Increment score for correct answers
                    }
                }
            }

            // Update the score in the UserQuiz table
            UserQuiz userQuiz = userQuizRepository.findByIdQuizAndIdUserParticipant(idQuiz, idUser);
            if (userQuiz != null) {
                userQuiz.setPunctaj(punctaj);
                userQuizRepository.save(userQuiz);
            }

            return punctaj;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
