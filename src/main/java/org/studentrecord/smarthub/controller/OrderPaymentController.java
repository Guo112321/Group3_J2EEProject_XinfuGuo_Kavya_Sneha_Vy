package org.studentrecord.smarthub.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.studentrecord.smarthub.model.Invoice;
import org.studentrecord.smarthub.model.Order;
import org.studentrecord.smarthub.model.OrderItem;
import org.studentrecord.smarthub.model.OrderStatus;
import org.studentrecord.smarthub.service.InvoiceService;

import java.util.List;

@Controller
public class OrderPaymentController {

    private final InvoiceService invoiceService;

    public OrderPaymentController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/payment/submit")
    public String processPayment(@RequestParam String paymentMethod,
                                 @RequestParam(required = false) String cardNumber,
                                 @RequestParam(required = false) String expiryDate,
                                 @RequestParam(required = false) String cvv,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        List<OrderItem> cartItems = (List<OrderItem>) session.getAttribute("cartItems");
        Order currentOrder = (Order) session.getAttribute("currentOrder");

        if (paymentMethod.equals("card")) {
            if (cardNumber == null || expiryDate == null || cvv == null) {
                redirectAttributes.addFlashAttribute("error", "Card details are required for card payment.");
                return "redirect:/payment";
            }

            // Dummy card validation logic (you can extend this later)
            if (cardNumber.length() < 12 || cvv.length() < 3) {
                redirectAttributes.addFlashAttribute("error", "Invalid card details.");
                return "redirect:/payment";
            }
        }

        // ✅ Mark order as paid
        currentOrder.setStatus(OrderStatus.PAID);
        // Optionally: orderService.saveOrder(currentOrder);

        // ✅ Generate invoice
        Invoice invoice = invoiceService.generateInvoice(currentOrder);

        // ✅ Clean up session
        session.removeAttribute("cartItems");
        session.removeAttribute("currentOrder");

        // ✅ Redirect to invoice display page
        return "redirect:/invoice.html?id=" + invoice.getId();
    }
}
