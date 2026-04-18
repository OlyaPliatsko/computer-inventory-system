package com.example.computer_inventory_system.dto;

import com.example.computer_inventory_system.model.Component;
import com.example.computer_inventory_system.model.Computer;

import java.util.List;

public class ExportData {

    private List<Computer> computers;
    private List<Component> components;

    public List<Computer> getComputers() {
        return computers;
    }

    public void setComputers(List<Computer> computers) {
        this.computers = computers;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
}