package com.example.computer_inventory_system.controller;

import com.example.computer_inventory_system.model.Component;
import com.example.computer_inventory_system.model.Computer;
import com.example.computer_inventory_system.model.User;
import com.example.computer_inventory_system.service.ComponentService;
import com.example.computer_inventory_system.service.ComputerService;
import com.example.computer_inventory_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ComputerController {

    private final ComputerService computerService;
    private final ComponentService componentService;
    private final UserService userService;

    public ComputerController(ComputerService computerService,
                              ComponentService componentService,
                              UserService userService) {
        this.computerService = computerService;
        this.componentService = componentService;
        this.userService = userService;
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName());
    }

    @GetMapping("/computers")
    public String computers(@RequestParam(required = false) String query,
                            Model model,
                            Authentication authentication) {

        User currentUser = getCurrentUser(authentication);
        List<Computer> computers = computerService.search(query, currentUser);
        List<Component> allComponents = componentService.getAllComponents(currentUser);

        Map<String, Long> componentsCountMap = computers.stream()
                .collect(Collectors.toMap(
                        Computer::getInventoryNumber,
                        computer -> allComponents.stream()
                                .filter(component -> component.getComputer() != null
                                        && computer.getInventoryNumber().equals(component.getComputer().getInventoryNumber()))
                                .count()
                ));

        long totalComponents = componentService.getTotalComponents(currentUser);

        long withComponents = computers.stream()
                .filter(computer -> componentsCountMap.get(computer.getInventoryNumber()) > 0)
                .count();

        long emptyComputers = computers.size() - withComponents;

        model.addAttribute("computers", computers);
        model.addAttribute("componentsCountMap", componentsCountMap);
        model.addAttribute("query", query);

        model.addAttribute("totalComputers", computerService.getTotalComputers(currentUser));
        model.addAttribute("totalComponents", totalComponents);
        model.addAttribute("withComponents", withComponents);
        model.addAttribute("emptyComputers", emptyComputers);

        return "computers";
    }

    @GetMapping("/computers/add")
    public String addComputerPage(Model model) {
        model.addAttribute("computer", new Computer());
        return "add-computer";
    }

    @PostMapping("/computers/add")
    public String addComputer(@Valid @ModelAttribute("computer") Computer computer,
                              BindingResult bindingResult,
                              Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "add-computer";
        }

        User currentUser = getCurrentUser(authentication);
        computer.setOwner(currentUser);

        computerService.save(computer);
        return "redirect:/computers";
    }

    @GetMapping("/computers/edit/{id}")
    public String editComputerPage(@PathVariable String id,
                                   Model model,
                                   Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        model.addAttribute("computer", computerService.getById(id, currentUser));
        return "edit-computer";
    }

    @PostMapping("/computers/edit")
    public String updateComputer(@Valid @ModelAttribute("computer") Computer computer,
                                 BindingResult bindingResult,
                                 Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "edit-computer";
        }

        User currentUser = getCurrentUser(authentication);
        Computer existingComputer = computerService.getById(computer.getInventoryNumber(), currentUser);

        if (existingComputer == null) {
            return "redirect:/computers";
        }

        computer.setOwner(currentUser);
        computerService.update(computer);

        return "redirect:/computers";
    }

    @GetMapping("/computers/delete/{id}")
    public String deleteComputer(@PathVariable String id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        computerService.deleteById(id, currentUser);
        return "redirect:/computers";
    }

    @GetMapping("/computers/search")
    @ResponseBody
    public List<Computer> searchComputers(@RequestParam(required = false) String query,
                                          Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        return computerService.search(query, currentUser);
    }

    @GetMapping("/computers/{id}/components")
    public String viewComputerComponents(@PathVariable String id,
                                         Model model,
                                         Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Computer computer = computerService.getById(id, currentUser);

        if (computer == null) {
            return "redirect:/computers";
        }

        List<Component> components = componentService.getByComputer(currentUser, computer);

        model.addAttribute("components", components);
        model.addAttribute("computerId", id);

        return "computer-components";
    }
}