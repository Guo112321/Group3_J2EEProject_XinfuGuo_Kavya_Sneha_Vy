package org.studentrecord.smarthub.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.Inventory;
import org.studentrecord.smarthub.model.Role;
import org.studentrecord.smarthub.model.User;
import org.studentrecord.smarthub.repository.UserRepository;
import org.studentrecord.smarthub.service.InventoryService;
import org.studentrecord.smarthub.service.UserService;

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

    @GetMapping("/login")
    public String login() {
        return "userLogin";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "userRegister";
    }

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

        Role role;
        if (user.getUsername().startsWith("storeOwner")) {
            role = Role.ROLE_STORE_OWNER;
        } else {
            role = Role.ROLE_CUSTOMER;
        }

        user.setRole(role);
        userService.registerUser(user);
        model.addAttribute("success", "Registration successful! Please log in.");
        return "userLogin";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password.");
            return "userLogin";
        }

        session.setAttribute("loggedInUser", user);

        if (user.getRole() == Role.ROLE_STORE_OWNER) {
            List<Inventory> inventoryList = inventoryService.getAllItems();
            model.addAttribute("inventory", inventoryList);
            return "storeOwnerDashboard";
        } else {
            List<Inventory> inventoryList = inventoryService.getAllItems();
            model.addAttribute("inventory", inventoryList);
            return "userDashboard";
        }
    }

    @GetMapping({"", "/"})
    public String userHome(Model model) {
        List<Inventory> inventoryList = inventoryService.getAllItems();
        model.addAttribute("inventory", inventoryList);
        return "userDashboard";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Inventory> searchResults = inventoryService.searchProductsByName(query);
        model.addAttribute("inventory", searchResults);
        return "userDashboard";
    }

    @GetMapping("/search/advanced")
    public String advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy,
            Model model
    ) {
        List<Inventory> filteredResults = inventoryService.advancedSearch(name, category, available, minPrice, maxPrice, sortBy);
        model.addAttribute("inventory", filteredResults);
        return "userDashboard";
    }
}
