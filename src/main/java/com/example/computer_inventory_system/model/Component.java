package com.example.computer_inventory_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Component {

    @Id
    @NotBlank(message = "Inventory number is required")
    private String inventoryNumber;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private ComponentType type;

    @Enumerated(EnumType.STRING)
    private ComponentStatus status;

    private String computerInventoryNumber;

    public Component() {
    }

    public Component(String inventoryNumber, String manufacturer, String model,
                     ComponentType type, ComponentStatus status, String computerInventoryNumber) {
        this.inventoryNumber = inventoryNumber;
        this.manufacturer = manufacturer;
        this.model = model;
        this.type = type;
        this.status = status;
        this.computerInventoryNumber = computerInventoryNumber;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
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

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public ComponentStatus getStatus() {
        return status;
    }

    public void setStatus(ComponentStatus status) {
        this.status = status;
    }

    public String getComputerInventoryNumber() {
        return computerInventoryNumber;
    }

    public void setComputerInventoryNumber(String computerInventoryNumber) {
        this.computerInventoryNumber = computerInventoryNumber;
    }
}