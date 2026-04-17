package com.example.computer_inventory_system.repository;

import com.example.computer_inventory_system.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComputerRepository extends JpaRepository<Computer, String> {

    List<Computer> findByNameContainingIgnoreCaseOrInventoryNumberContainingIgnoreCase(String name, String inventoryNumber);
}