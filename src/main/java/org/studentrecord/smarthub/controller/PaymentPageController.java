package org.studentrecord.smarthub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.Order;
import org.studentrecord.smarthub.service.OrderService;

@Controller
public class PaymentPageController {

    private final OrderService orderService;

    public PaymentPageController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("orderId") Long orderId,
                                  Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/user";
        }

        model.addAttribute("orderId", order.getId());
        model.addAttribute("totalAmount", order.getTotalAmount());
        model.addAttribute("orderStatus", order.getStatus());

        return "payment";
    }
}
