package com.example.computer_inventory_system.controller;

import com.example.computer_inventory_system.model.User;
import com.example.computer_inventory_system.service.ComponentService;
import com.example.computer_inventory_system.service.ComputerService;
import com.example.computer_inventory_system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    private final ComputerService computerService;
    private final ComponentService componentService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public PageController(ComputerService computerService,
                          ComponentService componentService,
                          UserService userService,
                          AuthenticationManager authenticationManager) {
        this.computerService = computerService;
        this.componentService = componentService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return userService.findByEmail(authentication.getName());
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model,
                               HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (user.getConfirmPassword() == null || !user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "register";
        }

        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("emailError", "A user with this email already exists");
            return "register";
        }

        String rawPassword = user.getPassword();

        userService.saveUser(user);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword);

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("totalComputers", computerService.getTotalComputers(currentUser));
        model.addAttribute("totalComponents", componentService.getTotalComponents(currentUser));
        model.addAttribute("assignedComponents", componentService.getAssignedComponents(currentUser));
        model.addAttribute("freeComponents", componentService.getFreeComponents(currentUser));

        return "dashboard";
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }
}