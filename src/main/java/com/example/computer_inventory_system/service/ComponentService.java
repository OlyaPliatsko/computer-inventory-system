package com.example.computer_inventory_system.service;

import com.example.computer_inventory_system.model.*;
import com.example.computer_inventory_system.repository.ComponentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService {

    private final ComponentRepository repository;

    public ComponentService(ComponentRepository repository) {
        this.repository = repository;
    }

    public List<Component> getAllComponents(User owner) {
        return repository.findByOwner(owner);
    }

    public void save(Component component) {
        if (component.getComputer() != null) {
            component.setStatus(ComponentStatus.ASSIGNED);
        } else if (component.getStatus() == null || component.getStatus() != ComponentStatus.IN_REPAIR) {
            component.setStatus(ComponentStatus.FREE);
        }

        repository.save(component);
    }

    public Component getById(String id, User owner) {
        return repository.findByInventoryNumberAndOwner(id, owner).orElse(null);
    }

    public void update(Component component) {
        if (component.getComputer() != null) {
            component.setStatus(ComponentStatus.ASSIGNED);
        } else if (component.getStatus() == null || component.getStatus() != ComponentStatus.IN_REPAIR) {
            component.setStatus(ComponentStatus.FREE);
        }

        repository.save(component);
    }

    public void deleteById(String id, User owner) {
        Component component = getById(id, owner);
        if (component != null) {
            repository.delete(component);
        }
    }

    public List<Component> filter(String query, String type, String status, User owner) {
        List<Component> components;

        if (query == null || query.isBlank()) {
            components = repository.findByOwner(owner);
        } else {
            components = repository.findByOwnerAndModelContainingIgnoreCaseOrOwnerAndInventoryNumberContainingIgnoreCase(
                    owner, query,
                    owner, query
            );
        }

        if (type != null && !type.isBlank()) {
            ComponentType componentType = ComponentType.valueOf(type);
            components = components.stream()
                    .filter(component -> component.getType() == componentType)
                    .toList();
        }

        if (status != null && !status.isBlank()) {
            ComponentStatus componentStatus = ComponentStatus.valueOf(status);
            components = components.stream()
                    .filter(component -> component.getStatus() == componentStatus)
                    .toList();
        }

        return components;
    }

    public long getTotalComponents(User owner) {
        return repository.findByOwner(owner).size();
    }

    public long getFreeComponents(User owner) {
        return repository.findByOwner(owner).stream()
                .filter(component -> component.getStatus() == ComponentStatus.FREE)
                .count();
    }

    public long getAssignedComponents(User owner) {
        return repository.findByOwner(owner).stream()
                .filter(component -> component.getStatus() == ComponentStatus.ASSIGNED)
                .count();
    }

    public long getRepairComponents(User owner) {
        return repository.findByOwner(owner).stream()
                .filter(component -> component.getStatus() == ComponentStatus.IN_REPAIR)
                .count();
    }

    public List<Component> getByComputer(User owner, Computer computer) {
        return repository.findByOwnerAndComputer(owner, computer);
    }
}