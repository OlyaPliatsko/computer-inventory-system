package com.example.computer_inventory_system.controller;

import com.example.computer_inventory_system.service.ComponentService;
import com.example.computer_inventory_system.service.ComputerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final ComputerService computerService;
    private final ComponentService componentService;

    public PageController(ComputerService computerService, ComponentService componentService) {
        this.computerService = computerService;
        this.componentService = componentService;
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
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalComputers", computerService.getTotalComputers());
        model.addAttribute("totalComponents", componentService.getTotalComponents());
        model.addAttribute("assignedComponents", componentService.getAssignedComponents());
        model.addAttribute("freeComponents", componentService.getFreeComponents());

        return "dashboard";
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }
}