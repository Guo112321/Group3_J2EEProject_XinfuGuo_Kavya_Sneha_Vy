package org.studentrecord.smarthub.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.studentrecord.smarthub.model.Inventory;
import org.studentrecord.smarthub.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllItems() {
        return inventoryRepository.findAll();
    }

    public Inventory getItemById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public Inventory addItem(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory updateItem(Long id, Inventory updatedInventory) {
        Optional<Inventory> existingItemOptional = inventoryRepository.findById(id);

        if (existingItemOptional.isPresent()) {
            Inventory existingItem = existingItemOptional.get();

            existingItem.setItemName(updatedInventory.getItemName());
            existingItem.setCategory(updatedInventory.getCategory());
            existingItem.setQuantity(updatedInventory.getQuantity());
            existingItem.setPrice(updatedInventory.getPrice());
            existingItem.setAvailable(updatedInventory.isAvailable());

            return inventoryRepository.save(existingItem);
        } else {
            return null;
        }
    }

    public void softDeleteItem(Long id) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            inventory.setAvailable(false);
            inventoryRepository.save(inventory);
        }
    }

    public void deleteItem(Long id) {
        inventoryRepository.deleteById(id);
    }

    // 🔍 Basic name search
    public List<Inventory> searchProductsByName(String query) {
        return inventoryRepository.findByItemNameContainingIgnoreCase(query);
    }

    // 🔎 Advanced search with filters and sort
    public List<Inventory> advancedSearch(String name, String category, Boolean available, Double minPrice, Double maxPrice, String sortBy) {
        Specification<Inventory> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("itemName")), "%" + name.toLowerCase() + "%"));
        }
        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }
        if (available != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("available"), available));
        }
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        Sort sort = Sort.unsorted();
        if ("price".equalsIgnoreCase(sortBy)) {
            sort = Sort.by("price").ascending();
        } else if ("name".equalsIgnoreCase(sortBy)) {
            sort = Sort.by("itemName").ascending();
        }

        return inventoryRepository.findAll(spec, sort);
    }
}
