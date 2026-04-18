package com.example.computer_inventory_system.service;

import com.example.computer_inventory_system.model.*;
import com.example.computer_inventory_system.repository.ComponentRepository;
import com.example.computer_inventory_system.repository.ComputerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService {

    private final ComponentRepository repository;
    private final ComputerRepository computerRepository;

    public ComponentService(ComponentRepository repository, ComputerRepository computerRepository) {
        this.repository = repository;
        this.computerRepository = computerRepository;

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

    public List<Component> filter(String query, String type, String status, String computerId, User owner) {
        List<Component> components = repository.findByOwner(owner);

        if (query != null && !query.isBlank()) {
            String lowerQuery = query.toLowerCase();

            components = components.stream()
                    .filter(component ->
                            component.getInventoryNumber().toLowerCase().contains(lowerQuery) ||
                                    component.getModel().toLowerCase().contains(lowerQuery) ||
                                    component.getManufacturer().toLowerCase().contains(lowerQuery))
                    .toList();
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

        if (computerId != null && !computerId.isBlank()) {
            if (computerId.equals("FREE_ONLY")) {
                components = components.stream()
                        .filter(component -> component.getComputer() == null)
                        .toList();
            } else {
                components = components.stream()
                        .filter(component -> component.getComputer() != null)
                        .filter(component -> component.getComputer().getInventoryNumber().equals(computerId))
                        .toList();
            }
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

    public boolean unassignComponent(String componentId, User owner) {
        Component component = repository.findByInventoryNumberAndOwner(componentId, owner).orElse(null);

        if (component == null) {
            return false;
        }

        if (component.getComputer() == null) {
            return false;
        }

        component.setComputer(null);
        component.setStatus(ComponentStatus.FREE);
        repository.save(component);

        return true;
    }

    public boolean transferComponent(String componentId, String newComputerId, User owner) {
        Component component = repository.findByInventoryNumberAndOwner(componentId, owner).orElse(null);

        if (component == null) {
            return false;
        }

        if (component.getComputer() == null) {
            return false;
        }

        Computer newComputer = computerRepository.findByInventoryNumberAndOwner(newComputerId, owner).orElse(null);

        if (newComputer == null) {
            return false;
        }

        if (component.getComputer().getInventoryNumber().equals(newComputerId)) {
            return false;
        }

        component.setComputer(newComputer);
        component.setStatus(ComponentStatus.ASSIGNED);
        repository.save(component);

        return true;
    }
}