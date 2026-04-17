package com.example.computer_inventory_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "component")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "computer_id")
    private Computer computer;

    public Component() {
    }

    public Component(String inventoryNumber, String manufacturer, String model,
                     ComponentType type, ComponentStatus status) {
        this.inventoryNumber = inventoryNumber;
        this.manufacturer = manufacturer;
        this.model = model;
        this.type = type;
        this.status = status;
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

    public User getOwner() {
        return owner;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
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
}