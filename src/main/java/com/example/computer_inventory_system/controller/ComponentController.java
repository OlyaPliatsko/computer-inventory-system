package com.example.computer_inventory_system.controller;

import com.example.computer_inventory_system.model.Component;
import com.example.computer_inventory_system.service.ComponentService;
import com.example.computer_inventory_system.service.ComputerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ComponentController {

    private final ComponentService componentService;
    private final ComputerService computerService;

    public ComponentController(ComponentService componentService, ComputerService computerService) {
        this.componentService = componentService;
        this.computerService = computerService;
    }

    @GetMapping("/components")
    public String components(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("components", componentService.filter(query, null, null));
        model.addAttribute("query", query);

        model.addAttribute("totalComponents", componentService.getTotalComponents());
        model.addAttribute("freeComponents", componentService.getFreeComponents());
        model.addAttribute("assignedComponents", componentService.getAssignedComponents());
        model.addAttribute("repairComponents", componentService.getRepairComponents());

        return "components";
    }

    @GetMapping("/components/add")
    public String addComponentPage(Model model) {
        model.addAttribute("component", new Component());
        model.addAttribute("computers", computerService.getAllComputers());
        return "add-component";
    }

    @PostMapping("/components/add")
    public String addComponent(@Valid @ModelAttribute("component") Component component,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("computers", computerService.getAllComputers());
            return "add-component";
        }

        componentService.save(component);
        return "redirect:/components";
    }

    @GetMapping("/components/edit/{id}")
    public String editComponentPage(@PathVariable String id, Model model) {
        Component component = componentService.getById(id);
        model.addAttribute("component", component);
        model.addAttribute("computers", computerService.getAllComputers());
        return "edit-component";
    }

    @PostMapping("/components/edit")
    public String updateComponent(@Valid @ModelAttribute("component") Component component,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("computers", computerService.getAllComputers());
            return "edit-component";
        }

        componentService.update(component);
        return "redirect:/components";
    }

    @GetMapping("/components/delete/{id}")
    public String deleteComponent(@PathVariable String id) {
        componentService.deleteById(id);
        return "redirect:/components";
    }

    @GetMapping("/components/search")
    @ResponseBody
    public List<Component> searchComponents(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {

        return componentService.filter(query, type, status);
    }

    @GetMapping("/operations/assign")
    public String assignPage(Model model) {
        model.addAttribute("components", componentService.getFreeComponentsList());
        model.addAttribute("computers", computerService.getAllComputers());
        return "assign-component";
    }

    @PostMapping("/operations/assign")
    public String assignComponent(@RequestParam String componentId,
                                  @RequestParam String computerId) {
        Component component = componentService.getById(componentId);

        if (component != null) {
            component.setComputerInventoryNumber(computerId);
            componentService.update(component);
        }
        return "redirect:/components";
    }
}