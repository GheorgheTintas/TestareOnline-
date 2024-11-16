package com.example.testareonline.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder Encoder = new BCryptPasswordEncoder();
        String s= Encoder.encode("test");
        System.out.println(s+"**********");
        return Encoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()// disables Cross-Site Request Forgery protection.
                // It is often disabled for stateless authentication mechanisms like token-based authentication.
                // It allows HTTP requests without the need for CSRF tokens.
                .authorizeRequests() //This method indicates that you're about to configure authorization rules for different URL patterns
                .antMatchers("/login").permitAll() // no need to auth to access the endpoint
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()// for any other request (anyRequest), the user must be authenticated to access it. In other words, to access any URL not explicitly configured with "permitAll," users must be logged in.
                .and()
                .logout()
                .logoutUrl("/logout") // Specify the URL for the logout endpoint
                .logoutSuccessUrl("/") // Redirect to the login page
                .invalidateHttpSession(true)
                .deleteCookies("*"); // Delete cookies
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}