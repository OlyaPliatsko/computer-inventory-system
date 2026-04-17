package com.example.computer_inventory_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Computer {

    @Id
    @NotBlank(message = "Inventory number is required")
    private String inventoryNumber;

    @NotBlank(message = "Computer name is required")
    private String name;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Responsible person is required")
    private String responsiblePerson;

    public Computer() {
    }

    public Computer(String inventoryNumber, String name, String manufacturer, String model,
                    String responsiblePerson, Integer componentsCount) {
        this.inventoryNumber = inventoryNumber;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.responsiblePerson = responsiblePerson;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }
}