package com.example.computer_inventory_system.repository;

import com.example.computer_inventory_system.model.Computer;
import com.example.computer_inventory_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComputerRepository extends JpaRepository<Computer, String> {

    List<Computer> findByOwner(User owner);

    List<Computer> findByOwnerAndNameContainingIgnoreCaseOrOwnerAndInventoryNumberContainingIgnoreCase(
            User owner1, String name,
            User owner2, String inventoryNumber
    );

    Optional<Computer> findByInventoryNumberAndOwner(String inventoryNumber, User owner);
}