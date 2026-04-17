package com.example.computer_inventory_system.controller;

import com.example.computer_inventory_system.model.User;
import com.example.computer_inventory_system.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserRepository userRepository;

    public GlobalControllerAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("loggedInUserName")
    public String loggedInUserName(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            String email = authentication.getName();

            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                return user.getName();
            }
        }

        return null;
    }
}