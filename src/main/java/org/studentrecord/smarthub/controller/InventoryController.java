package org.studentrecord.smarthub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.Inventory;
import org.studentrecord.smarthub.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Public endpoints accessible by all users
    @GetMapping
    public List<Inventory> getAllItems() {
        return inventoryService.getAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getItemById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getItemById(id);
        return inventory != null ? ResponseEntity.ok(inventory) : ResponseEntity.notFound().build();
    }

    // Admin-only endpoints for adding, updating, and deleting items
    @PostMapping
    public ResponseEntity<Inventory> addItem(@RequestBody Inventory inventory) {
        // Ideally, add role-based checks here (e.g., if the user is an admin)
        return ResponseEntity.ok(inventoryService.addItem(inventory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateItem(@PathVariable Long id, @RequestBody Inventory updatedInventory) {
        // Ideally, add role-based checks here
        Inventory inventory = inventoryService.updateItem(id, updatedInventory);
        return inventory != null ? ResponseEntity.ok(inventory) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        // Ideally, add role-based checks here
        inventoryService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}