package com.example.computer_inventory_system.controller;

import com.example.computer_inventory_system.dto.ExportData;
import com.example.computer_inventory_system.model.Component;
import com.example.computer_inventory_system.model.Computer;
import com.example.computer_inventory_system.model.User;
import com.example.computer_inventory_system.service.ComponentService;
import com.example.computer_inventory_system.service.ComputerService;
import com.example.computer_inventory_system.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/data")
public class DataController {

    private final ComputerService computerService;
    private final ComponentService componentService;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DataController(ComputerService computerService,
                          ComponentService componentService,
                          UserService userService) {
        this.computerService = computerService;
        this.componentService = componentService;
        this.userService = userService;
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return userService.findByEmail(authentication.getName());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(Authentication authentication) throws Exception {
        User currentUser = getCurrentUser(authentication);

        ExportData exportData = new ExportData();
        exportData.setComputers(computerService.getAllComputers(currentUser));
        exportData.setComponents(componentService.getAllComponents(currentUser));

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(exportData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=computer_inventory_export.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.getBytes(StandardCharsets.UTF_8));
    }

    @PostMapping("/import")
    public String importData(@RequestParam("file") MultipartFile file,
                             Authentication authentication) throws Exception {
        User currentUser = getCurrentUser(authentication);

        ExportData importData = objectMapper.readValue(file.getInputStream(), ExportData.class);

        if (importData.getComputers() != null) {
            for (Computer computer : importData.getComputers()) {
                computer.setOwner(currentUser);
                computerService.save(computer);
            }
        }

        if (importData.getComponents() != null) {
            for (Component component : importData.getComponents()) {
                component.setOwner(currentUser);
                componentService.save(component);
            }
        }

        return "redirect:/dashboard?importSuccess=true";
    }
}