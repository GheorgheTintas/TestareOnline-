package com.example.testareonline.controller;

import com.example.testareonline.dto.CredentialeDTO;
import com.example.testareonline.model.User;
import com.example.testareonline.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    /**
     * Authentication authenticate = authenticationManager.authenticate(...)
     * <p>
     * It attempts to authenticate the user by using the authenticationManager.
     * <p>
     * It creates an Authentication object with the provided UsernamePasswordAuthenticationToken,
     * <p>
     * which contains the user's login credentials.
     * <p>
     * <p>
     * <p>
     * SecurityContextHolder.getContext().setAuthentication(authenticate)
     * <p>
     * After successful authentication, the authenticated Authentication object is set in the security context.
     * <p>
     * This step effectively logs the user in.
     *
     * @param credentialeDTO obiect de transfer continand credentialele care trebuie verificate
     * @return HTTP STATUS CREATED
     */

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request, HttpServletResponse response,
                                        @RequestBody CredentialeDTO credentialeDTO) {
        System.out.println("Credentiale introduse:" + credentialeDTO.getUsername() + " " + credentialeDTO.getParola());
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(credentialeDTO.getUsername(),
                        credentialeDTO.getParola()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        Optional<User> userOptional = userRepository.findByUsername(credentialeDTO.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Cookie userCookie = new Cookie("userId", String.valueOf(user.getId()));
            userCookie.setMaxAge(-1); // cookie valabil pe durata sesiunii
            userCookie.setPath("/");
            response.addCookie(userCookie);
            return new ResponseEntity<>("Autentificare reusita!", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Autentificare esuata!", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("Delogare reusita!", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody CredentialeDTO credentialeDTO) {
        // Check if the username already exists
        if (userRepository.existsByUsername(credentialeDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Numele de utilizator exista!");
        }

        // Hash the password using BCrypt
        String hashedPassword = passwordEncoder.encode(credentialeDTO.getParola());

        // Create and save the new user
        User user = new User();
        user.setUsername(credentialeDTO.getUsername());
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);

        return ResponseEntity.ok("Inregistrare reusita!");
    }
}
