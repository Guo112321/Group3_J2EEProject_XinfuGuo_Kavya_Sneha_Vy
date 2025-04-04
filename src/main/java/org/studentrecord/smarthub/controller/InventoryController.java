package org.studentrecord.smarthub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.Inventory;
import org.studentrecord.smarthub.service.InventoryService;

import java.util.List;

@Controller
@RequestMapping("/store-owner/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String listInventory(Model model) {
        List<Inventory> inventoryList = inventoryService.getAllItems();
        System.out.println("Fetched Inventory: " + inventoryList); // Debugging
        model.addAttribute("inventory", inventoryList);
        return "storeOwnerDashboard";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        return "add-inventory";
    }

    @PostMapping("/add")
    public String addInventory(@ModelAttribute Inventory inventory) {
        inventoryService.addItem(inventory);
        return "redirect:/store-owner/inventory";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Inventory inventory = inventoryService.getItemById(id);
        if (inventory != null) {
            model.addAttribute("inventory", inventory);
            return "edit-inventory";
        }
        return "redirect:/store-owner/inventory";
    }

    @PostMapping("/edit/{id}")
    public String updateInventory(@PathVariable Long id, @ModelAttribute Inventory inventory) {
        inventoryService.updateItem(id, inventory);
        return "redirect:/store-owner/inventory";
    }

    // InventoryController.java
    @GetMapping("/delete/{id}")
    public String deleteInventory(@PathVariable Long id) {
        inventoryService.softDeleteItem(id); // Perform soft delete
        return "redirect:/store-owner/inventory";
    }
    @GetMapping("/search")
    public String searchInventory(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy,
            Model model
    ) {
        List<Inventory> results = inventoryService.advancedSearch(name, category, available, minPrice, maxPrice, sortBy);
        model.addAttribute("inventory", results);
        return "storeOwnerDashboard";
    }




}