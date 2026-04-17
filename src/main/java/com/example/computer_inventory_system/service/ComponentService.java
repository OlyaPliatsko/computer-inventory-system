package com.example.computer_inventory_system.service;

import com.example.computer_inventory_system.model.Component;
import com.example.computer_inventory_system.model.ComponentStatus;
import com.example.computer_inventory_system.model.ComponentType;
import com.example.computer_inventory_system.repository.ComponentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService {

    private final ComponentRepository repository;

    public ComponentService(ComponentRepository repository) {
        this.repository = repository;
    }

    public List<Component> getAllComponents() {
        return repository.findAll();
    }

    public List<Component> getFreeComponentsList() {
        return repository.findByStatus(ComponentStatus.FREE);
    }

    public void save(Component component) {
        if (component.getComputerInventoryNumber() != null &&
                !component.getComputerInventoryNumber().isBlank()) {
            component.setStatus(ComponentStatus.ASSIGNED);
        } else {
            component.setStatus(ComponentStatus.FREE);
        }

        repository.save(component);
    }

    public Component getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void update(Component component) {
        if (component.getComputerInventoryNumber() != null &&
                !component.getComputerInventoryNumber().isBlank()) {
            component.setStatus(ComponentStatus.ASSIGNED);
        } else {
            component.setStatus(ComponentStatus.FREE);
        }
        repository.save(component);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<Component> filter(String query, String type, String status) {
        boolean hasQuery = query != null && !query.isBlank();
        boolean hasType = type != null && !type.isBlank();
        boolean hasStatus = status != null && !status.isBlank();

        if (!hasQuery && !hasType && !hasStatus) {
            return repository.findAll();
        }

        if (hasType && hasStatus && hasQuery) {
            return repository.findByTypeAndStatusAndModelContainingIgnoreCaseOrTypeAndStatusAndInventoryNumberContainingIgnoreCase(
                    ComponentType.valueOf(type), ComponentStatus.valueOf(status), query,
                    ComponentType.valueOf(type), ComponentStatus.valueOf(status), query
            );
        }

        if (hasType && hasQuery) {
            return repository.findByTypeAndModelContainingIgnoreCaseOrTypeAndInventoryNumberContainingIgnoreCase(
                    ComponentType.valueOf(type), query,
                    ComponentType.valueOf(type), query
            );
        }

        if (hasStatus && hasQuery) {
            return repository.findByStatusAndModelContainingIgnoreCaseOrStatusAndInventoryNumberContainingIgnoreCase(
                    ComponentStatus.valueOf(status), query,
                    ComponentStatus.valueOf(status), query
            );
        }

        if (hasType && hasStatus) {
            return repository.findByTypeAndStatus(
                    ComponentType.valueOf(type),
                    ComponentStatus.valueOf(status)
            );
        }

        if (hasType) {
            return repository.findByType(ComponentType.valueOf(type));
        }

        if (hasStatus) {
            return repository.findByStatus(ComponentStatus.valueOf(status));
        }

        return repository.findByModelContainingIgnoreCaseOrInventoryNumberContainingIgnoreCase(query, query);
    }

    public long getTotalComponents() {
        return repository.count();
    }

    public long getFreeComponents() {
        return repository.findAll().stream()
                .filter(component -> component.getStatus() == ComponentStatus.FREE)
                .count();
    }

    public long getAssignedComponents() {
        return repository.findAll().stream()
                .filter(component -> component.getStatus() == ComponentStatus.ASSIGNED)
                .count();
    }

    public long getRepairComponents() {
        return repository.findAll().stream()
                .filter(component -> component.getStatus() == ComponentStatus.IN_REPAIR)
                .count();
    }
}