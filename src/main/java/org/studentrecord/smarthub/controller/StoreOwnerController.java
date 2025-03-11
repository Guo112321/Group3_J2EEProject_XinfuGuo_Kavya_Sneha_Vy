package org.studentrecord.smarthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.Role;
import org.studentrecord.smarthub.model.User;
import org.studentrecord.smarthub.repository.RoleRepository;
import org.studentrecord.smarthub.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/store-owner")
public class StoreOwnerController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    // Display login page for store owners
    @GetMapping("/login")
    public String login() {
        return "storeOwnerLogin";
    }

    // Display register page for store owners
    @GetMapping("/register")
    public String register() {
        return "storeOwnerRegister";
    }

    // Handle store owner registration form submission
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute User user) {
        // Check if a user with the same username already exists
        if (userService.findByUsername(user.getUsername()) != null) {
            // Redirect back to the registration page with an error message
            return "redirect:/store-owner/register?error=usernameExists";
        }

        Set<Role> roles = new HashSet<>();
        Role ownerRole = roleRepository.findByName("ROLE_STORE_OWNER");
        if (ownerRole != null) {
            roles.add(ownerRole);
        }
        user.setRoles(roles);

        // Check if a user with the same email already exists
        if (userService.findByEmail(user.getEmail()) != null) {
            // Redirect back to the registration page with an error message
            return "redirect:/store-owner/register?error=emailExists";
        }

        // If no duplicate found, proceed with registration
        userService.registerUser(user);
        return "redirect:/store-owner/login";
    }


    // Handle store owner login form submission
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password) {
        // Fetch user by username
        User user = userService.findByUsername(username);

        if (user != null) {
            // Validate password
            if (user.getPassword().equals(password)) {
                return "redirect:/store-owner/dashboard";
            } else {
                // Incorrect password
                return "redirect:/store-owner/login?error=invalidPassword";
            }
        } else {
            // User not found
            return "redirect:/store-owner/login?error=userNotFound";
        }
    }


    // Store owner dashboard page (for managing store)
    @GetMapping("/dashboard")
    public String storeOwnerDashboard() {
        return "storeOwnerDashboard";
    }
}
