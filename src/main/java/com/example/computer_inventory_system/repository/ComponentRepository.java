package com.example.computer_inventory_system.repository;

import com.example.computer_inventory_system.model.Component;
import com.example.computer_inventory_system.model.ComponentStatus;
import com.example.computer_inventory_system.model.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, String> {

    List<Component> findByModelContainingIgnoreCaseOrInventoryNumberContainingIgnoreCase(String model, String inventoryNumber);

    List<Component> findByType(ComponentType type);

    List<Component> findByStatus(ComponentStatus status);

    List<Component> findByTypeAndStatus(ComponentType type, ComponentStatus status);

    List<Component> findByTypeAndModelContainingIgnoreCaseOrTypeAndInventoryNumberContainingIgnoreCase(
            ComponentType type, String model, ComponentType type2, String inventoryNumber
    );

    List<Component> findByStatusAndModelContainingIgnoreCaseOrStatusAndInventoryNumberContainingIgnoreCase(
            ComponentStatus status, String model, ComponentStatus status2, String inventoryNumber
    );

    List<Component> findByTypeAndStatusAndModelContainingIgnoreCaseOrTypeAndStatusAndInventoryNumberContainingIgnoreCase(
            ComponentType type, ComponentStatus status, String model,
            ComponentType type2, ComponentStatus status2, String inventoryNumber
    );
}