package com.example.computer_inventory_system.controller;

import com.example.computer_inventory_system.model.Component;
import com.example.computer_inventory_system.model.ComponentStatus;
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

@Controller
public class ComponentController {

    private final ComponentService componentService;
    private final ComputerService computerService;
    private final UserService userService;

    public ComponentController(ComponentService componentService,
                               ComputerService computerService,
                               UserService userService) {
        this.componentService = componentService;
        this.computerService = computerService;
        this.userService = userService;
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName());
    }

    @GetMapping("/components")
    public String components(@RequestParam(required = false) String query,
                             Model model,
                             Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        model.addAttribute("components", componentService.filter(query, null, null, null, currentUser));
        model.addAttribute("query", query);

        model.addAttribute("totalComponents", componentService.getTotalComponents(currentUser));
        model.addAttribute("freeComponents", componentService.getFreeComponents(currentUser));
        model.addAttribute("assignedComponents", componentService.getAssignedComponents(currentUser));
        model.addAttribute("repairComponents", componentService.getRepairComponents(currentUser));

        return "components";
    }

    @GetMapping("/components/add")
    public String addComponentPage(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        model.addAttribute("component", new Component());
        model.addAttribute("computers", computerService.getAllComputers(currentUser));

        return "add-component";
    }

    @PostMapping("/components/add")
    public String addComponent(@Valid @ModelAttribute("component") Component component,
                               BindingResult bindingResult,
                               @RequestParam(required = false) String computerInventoryNumber,
                               Model model,
                               Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        if (bindingResult.hasErrors()) {
            model.addAttribute("computers", computerService.getAllComputers(currentUser));
            return "add-component";
        }

        component.setOwner(currentUser);

        if (computerInventoryNumber != null && !computerInventoryNumber.isBlank()) {
            Computer computer = computerService.getById(computerInventoryNumber, currentUser);
            component.setComputer(computer);
        } else {
            component.setComputer(null);
        }

        componentService.save(component);
        return "redirect:/components";
    }

    @GetMapping("/components/edit/{id}")
    public String editComponentPage(@PathVariable String id,
                                    Model model,
                                    Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        Component component = componentService.getById(id, currentUser);
        model.addAttribute("component", component);
        model.addAttribute("computers", computerService.getAllComputers(currentUser));

        return "edit-component";
    }

    @PostMapping("/components/edit")
    public String updateComponent(@Valid @ModelAttribute("component") Component component,
                                  BindingResult bindingResult,
                                  @RequestParam(required = false) String computerInventoryNumber,
                                  Model model,
                                  Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        if (bindingResult.hasErrors()) {
            model.addAttribute("computers", computerService.getAllComputers(currentUser));
            return "edit-component";
        }

        component.setOwner(currentUser);

        if (computerInventoryNumber != null && !computerInventoryNumber.isBlank()) {
            Computer computer = computerService.getById(computerInventoryNumber, currentUser);
            component.setComputer(computer);
        } else {
            component.setComputer(null);
        }

        componentService.update(component);
        return "redirect:/components";
    }

    @GetMapping("/components/delete/{id}")
    public String deleteComponent(@PathVariable String id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        componentService.deleteById(id, currentUser);
        return "redirect:/components";
    }

    @GetMapping("/components/search")
    @ResponseBody
    public List<Component> searchComponents(@RequestParam(required = false) String query,
                                            @RequestParam(required = false) String type,
                                            @RequestParam(required = false) String status,
                                            @RequestParam(required = false) String computerId,
                                            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        return componentService.filter(query, type, status, computerId, currentUser);
    }

    @GetMapping("/operations/assign")
    public String assignPage(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        model.addAttribute("components", componentService.getAllComponents(currentUser)
                .stream()
                .filter(component -> component.getStatus() == ComponentStatus.FREE)
                .filter(component -> component.getComputer() == null)
                .toList());

        model.addAttribute("computers", computerService.getAllComputers(currentUser));
        return "assign-component";
    }

    @PostMapping("/operations/assign")
    public String assignComponent(@RequestParam String componentId,
                                  @RequestParam String computerId,
                                  Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        Component component = componentService.getById(componentId, currentUser);
        Computer computer = computerService.getById(computerId, currentUser);

        if (component == null || computer == null) {
            return "redirect:/operations/assign?error=true";
        }

        if (component.getStatus() != ComponentStatus.FREE || component.getComputer() != null) {
            return "redirect:/operations/assign?alreadyAssigned=true";
        }

        component.setComputer(computer);
        componentService.update(component);

        return "redirect:/components?assignedSuccess=true";
    }

    @PostMapping("/components/unassign/{inventoryNumber}")
    public String unassignComponent(@PathVariable String inventoryNumber,
                                    Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        boolean success = componentService.unassignComponent(inventoryNumber, currentUser);

        if (!success) {
            return "redirect:/components?unassignError=true";
        }

        return "redirect:/components?unassignSuccess=true";
    }

    @GetMapping("/operations/transfer")
    public String transferPage(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        model.addAttribute("components", componentService.getAllComponents(currentUser)
                .stream()
                .filter(component -> component.getComputer() != null)
                .toList());

        model.addAttribute("computers", computerService.getAllComputers(currentUser));

        return "transfer-component";
    }

    @PostMapping("/operations/transfer")
    public String transferComponent(@RequestParam String componentId,
                                    @RequestParam String computerId,
                                    Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        boolean success = componentService.transferComponent(componentId, computerId, currentUser);

        if (!success) {
            return "redirect:/operations/transfer?error=true";
        }
        return "redirect:/components?transferSuccess=true";
    }
}