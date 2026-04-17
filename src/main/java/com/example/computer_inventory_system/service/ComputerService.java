package com.example.computer_inventory_system.service;

import com.example.computer_inventory_system.model.Computer;
import com.example.computer_inventory_system.model.User;
import com.example.computer_inventory_system.repository.ComputerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputerService {

    private final ComputerRepository repository;

    public ComputerService(ComputerRepository repository) {
        this.repository = repository;
    }

    public List<Computer> getAllComputers(User owner) {
        return repository.findByOwner(owner);
    }

    public void save(Computer computer) {
        repository.save(computer);
    }

    public Computer getById(String id, User owner) {
        return repository.findByInventoryNumberAndOwner(id, owner).orElse(null);
    }

    public void update(Computer computer) {
        repository.save(computer);
    }

    public void deleteById(String id, User owner) {
        Computer computer = getById(id, owner);
        if (computer != null) {
            repository.delete(computer);
        }
    }

    public List<Computer> search(String query, User owner) {
        if (query == null || query.isBlank()) {
            return repository.findByOwner(owner);
        }

        return repository.findByOwnerAndNameContainingIgnoreCaseOrOwnerAndInventoryNumberContainingIgnoreCase(
                owner, query,
                owner, query
        );
    }

    public long getTotalComputers(User owner) {
        return repository.findByOwner(owner).size();
    }
}