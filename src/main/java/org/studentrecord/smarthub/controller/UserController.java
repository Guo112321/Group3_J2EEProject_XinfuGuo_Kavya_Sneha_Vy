package org.studentrecord.smarthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.Role;
import org.studentrecord.smarthub.model.User;
import org.studentrecord.smarthub.service.UserService;
import org.studentrecord.smarthub.repository.RoleRepository;
import org.studentrecord.smarthub.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    // Display login page
    @GetMapping("/login")
    public String login() {
        return "userLogin";
    }

    // Display register page
    @GetMapping("/register")
    public String register() {
        return "userRegister";
    }

    // Handle registration form submission with error messages
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

        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");
        if (customerRole != null) {
            roles.add(customerRole);
        }
        user.setRoles(roles);

        userService.registerUser(user);
        model.addAttribute("success", "Registration successful! Please log in.");
        return "userLogin";
    }

    // Handle login form submission with error messages
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password.");
            return "userLogin";
        }

        return "redirect:/user";
    }

    // Home page (for user dashboard)
    @GetMapping(value = {"", "/"})
    public String userHome() {
        return "userDashboard";
    }
}
