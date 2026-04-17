package com.example.computer_inventory_system.service;

import com.example.computer_inventory_system.model.Computer;
import com.example.computer_inventory_system.repository.ComputerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputerService {

    private final ComputerRepository repository;

    public ComputerService(ComputerRepository repository) {
        this.repository = repository;
    }

    public List<Computer> getAllComputers() {
        return repository.findAll();
    }

    public void save(Computer computer) {
        repository.save(computer);
    }

    public Computer getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void update(Computer computer) {
        repository.save(computer);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<Computer> search(String query) {
        if (query == null || query.isBlank()) {
            return repository.findAll();
        }
        return repository.findByNameContainingIgnoreCaseOrInventoryNumberContainingIgnoreCase(query, query);
    }

    public long getTotalComputers() {
        return repository.count();
    }
}