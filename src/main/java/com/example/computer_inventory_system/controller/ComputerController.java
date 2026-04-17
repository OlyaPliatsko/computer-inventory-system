package com.example.computer_inventory_system.controller;

import com.example.computer_inventory_system.model.Component;
import com.example.computer_inventory_system.model.Computer;
import com.example.computer_inventory_system.service.ComponentService;
import com.example.computer_inventory_system.service.ComputerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@Controller
public class ComputerController {

    private final ComputerService computerService;
    private final ComponentService componentService;

    public ComputerController(ComputerService computerService, ComponentService componentService) {
        this.computerService = computerService;
        this.componentService = componentService;
    }

    @GetMapping("/computers")
    public String computers(@RequestParam(required = false) String query, Model model) {
        List<Computer> computers = computerService.search(query);

        List<Component> allComponents = componentService.getAllComponents();

        Map<String, Long> componentsCountMap = computers.stream()
                .collect(Collectors.toMap(
                        Computer::getInventoryNumber,
                        computer -> allComponents.stream()
                                .filter(component -> computer.getInventoryNumber().equals(component.getComputerInventoryNumber()))
                                .count()
                ));

        long totalComponents = componentService.getTotalComponents();

        long withComponents = computers.stream()
                .filter(computer -> componentsCountMap.get(computer.getInventoryNumber()) > 0)
                .count();

        long emptyComputers = computers.size() - withComponents;

        model.addAttribute("computers", computers);
        model.addAttribute("componentsCountMap", componentsCountMap);
        model.addAttribute("query", query);

        model.addAttribute("totalComputers", computerService.getTotalComputers());
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
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-computer";
        }

        computerService.save(computer);
        return "redirect:/computers";
    }

    @GetMapping("/computers/edit/{id}")
    public String editComputerPage(@PathVariable String id, Model model) {
        model.addAttribute("computer", computerService.getById(id));
        return "edit-computer";
    }

    @PostMapping("/computers/edit")
    public String updateComputer(@Valid @ModelAttribute("computer") Computer computer,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-computer";
        }

        computerService.update(computer);
        return "redirect:/computers";
    }

    @GetMapping("/computers/delete/{id}")
    public String deleteComputer(@PathVariable String id) {
        componentService.getAllComponents().stream()
                .filter(component -> id.equals(component.getComputerInventoryNumber()))
                .forEach(component -> {
                    component.setComputerInventoryNumber(null);
                    componentService.update(component);
                });

        computerService.deleteById(id);
        return "redirect:/computers";
    }

    @GetMapping("/computers/search")
    @ResponseBody
    public List<Computer> searchComputers(@RequestParam(required = false) String query) {
        return computerService.search(query);
    }

    @GetMapping("/computers/{id}/components")
    public String viewComputerComponents(@PathVariable String id, Model model) {
        List<Component> components = componentService.getAllComponents()
                .stream()
                .filter(component -> id.equals(component.getComputerInventoryNumber()))
                .toList();

        model.addAttribute("components", components);
        model.addAttribute("computerId", id);

        return "computer-components";
    }
}