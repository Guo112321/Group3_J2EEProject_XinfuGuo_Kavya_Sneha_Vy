package org.studentrecord.smarthub.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.studentrecord.smarthub.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PaymentPageController {

    @GetMapping("/payment")
    public String showPaymentPage(Model model, HttpSession session) {
        List<OrderItem> cartItems = (List<OrderItem>) session.getAttribute("cartItems");
        double totalAmount = 0.0;

        if (cartItems != null) {
            for (OrderItem item : cartItems) {
                if (item.getInventory() != null) {
                    totalAmount += item.getInventory().getPrice() * item.getQuantity();
                }
            }
        } else {
            cartItems = new ArrayList<>();
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "payment"; // must match `payment.html` in templates
    }

}