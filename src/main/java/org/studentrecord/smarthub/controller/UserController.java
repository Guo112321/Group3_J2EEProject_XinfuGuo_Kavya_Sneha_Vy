package org.studentrecord.smarthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.Inventory;
import org.studentrecord.smarthub.model.Role;
import org.studentrecord.smarthub.model.User;
import org.studentrecord.smarthub.service.InventoryService;
import org.studentrecord.smarthub.service.UserService;
import org.studentrecord.smarthub.repository.UserRepository;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryService inventoryService;

    // Display login page for both user and store owner
    @GetMapping("/login")
    public String login() {
        return "userLogin";
    }

    // Display register page for both user and store owner
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "userRegister";
    }

    // Handle registration form submission for both user and store owner
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute User user, Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists. Please choose a different one.");
            return "userRegister";
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email already registered. Please use a different email.");
            return "userRegister";
        }

        // Assign role directly based on the username
        Role role;
        if (user.getUsername().startsWith("storeOwner")) {
            // Assign store owner role
            role = Role.ROLE_STORE_OWNER; // assuming you have added a STORE_OWNER enum value
        } else {
            // Assign customer role
            role = Role.ROLE_CUSTOMER; // assuming you have a CUSTOMER enum value
        }

        user.setRole(role); // Set the role directly (no repository needed)

        userService.registerUser(user);
        model.addAttribute("success", "Registration successful! Please log in.");
        return "userLogin";
    }

    // Handle login form submission for both user and store owner
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password.");
            return "userLogin";
        }

        // Check the user's role and redirect to the respective dashboard
        if (user.getRole() == Role.ROLE_STORE_OWNER) {
            // If store owner, redirect to store owner dashboard
            List<Inventory> inventoryList = inventoryService.getAllItems();
            model.addAttribute("inventory", inventoryList);
            return "storeOwnerDashboard";
        } else {
            // If regular user, redirect to user dashboard
            List<Inventory> inventoryList = inventoryService.getAllItems();
            model.addAttribute("inventory", inventoryList);
            return "userDashboard";
        }
    }

    // Home page (for user dashboard or store owner dashboard)
    @GetMapping(value = {"", "/"})
    public String userHome(Model model) {
        List<Inventory> inventoryList = inventoryService.getAllItems(); // Get all inventory items
        model.addAttribute("inventory", inventoryList);
        return "userDashboard"; // Default dashboard for regular user
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Inventory> searchResults = inventoryService.searchProductsByName(query);
        model.addAttribute("inventory", searchResults);
        return "userDashboard"; // Update to the correct view for the user dashboard
    }
}