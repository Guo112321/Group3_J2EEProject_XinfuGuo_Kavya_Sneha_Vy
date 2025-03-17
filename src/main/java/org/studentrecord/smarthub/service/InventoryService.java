package org.studentrecord.smarthub.service;

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


    // Update item
    public Inventory updateItem(Long id, Inventory updatedInventory) {
        Optional<Inventory> existingItemOptional = inventoryRepository.findById(id);

        if (existingItemOptional.isPresent()) {
            Inventory existingItem = existingItemOptional.get();

            // Update the existing item with the new values
            existingItem.setItemName(updatedInventory.getItemName()); // Corrected getter and setter
            existingItem.setCategory(updatedInventory.getCategory()); // Corrected getter and setter
            existingItem.setQuantity(updatedInventory.getQuantity()); // Corrected getter and setter
            existingItem.setPrice(updatedInventory.getPrice()); // Corrected getter and setter
            existingItem.setAvailable(updatedInventory.isAvailable()); // Corrected setter for boolean

            // Save the updated item
            return inventoryRepository.save(existingItem);
        } else {
            return null; // or throw an exception if you want to handle not found cases
        }
    }

    // InventoryService.java
    public void softDeleteItem(Long id) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            inventory.setAvailable(false); // Set available to false for soft delete
            inventoryRepository.save(inventory); // Save the updated item
        }
    }


    public void deleteItem(Long id) {
        inventoryRepository.deleteById(id);
    }

    //USER
    // Search for products by name
    public List<Inventory> searchProductsByName(String query) {
        return inventoryRepository.findByItemNameContainingIgnoreCase(query); // Find products with names containing the search query (case-insensitive)
    }
}
