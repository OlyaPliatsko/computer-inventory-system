package com.example.computer_inventory_system.repository;

import com.example.computer_inventory_system.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComponentRepository extends JpaRepository<Component, String> {

    List<Component> findByOwner(User owner);

    List<Component> findByOwnerAndModelContainingIgnoreCaseOrOwnerAndInventoryNumberContainingIgnoreCase(
            User owner1, String model,
            User owner2, String inventoryNumber
    );

    List<Component> findByOwnerAndType(User owner, ComponentType type);

    List<Component> findByOwnerAndStatus(User owner, ComponentStatus status);

    List<Component> findByOwnerAndTypeAndStatus(User owner, ComponentType type, ComponentStatus status);

    Optional<Component> findByInventoryNumberAndOwner(String inventoryNumber, User owner);

    List<Component> findByOwnerAndComputer(User owner, Computer computer);

    List<Component> findByOwnerAndManufacturerContainingIgnoreCase(User owner, String manufacturer);

    List<Component> findByOwnerAndComputerInventoryNumber(User owner, String computerInventoryNumber);
}