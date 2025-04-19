package org.studentrecord.smarthub.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.*;
import org.studentrecord.smarthub.service.InventoryService;
import org.studentrecord.smarthub.service.InvoiceService;
import org.studentrecord.smarthub.service.OrderItemService;
import org.studentrecord.smarthub.service.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final InvoiceService invoiceService;

    public CartController(InventoryService inventoryService,
                          OrderService orderService,
                          OrderItemService orderItemService,
                          InvoiceService invoiceService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);

        double total = 0;
        for (OrderItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }
        model.addAttribute("total", total);

        return "cart"; // renders cart.html
    }

    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam Long inventoryId,
                            @RequestParam int quantity,
                            HttpSession session) {

        Inventory inventory = inventoryService.getItemById(inventoryId);
        if (inventory == null || quantity <= 0) {
            return "Invalid item!";
        }

        if (quantity > inventory.getQuantity()) {
            return "Not enough stock available.";
        }

        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        boolean found = false;
        for (OrderItem item : cart) {
            if (item.getInventory().getId().equals(inventoryId)) {
                int newQty = item.getQuantity() + quantity;
                if (newQty > inventory.getQuantity()) {
                    return "Exceeds available stock.";
                }
                item.setQuantity(newQty);
                found = true;
                break;
            }
        }
        if (!found) {
            OrderItem item = new OrderItem();
            item.setInventory(inventory);
            item.setQuantity(quantity);
            item.setPrice(inventory.getPrice());
            cart.add(item);
        }

        session.setAttribute("cart", cart);
        return "Added to cart!";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long inventoryId,
                                 @RequestParam int quantity,
                                 HttpSession session) {

        Inventory inventory = inventoryService.getItemById(inventoryId);
        if (inventory == null || quantity <= 0 || quantity > inventory.getQuantity()) {
            return "redirect:/cart";
        }

        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart != null) {
            for (OrderItem item : cart) {
                if (item.getInventory().getId().equals(inventoryId)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long inventoryId, HttpSession session) {
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getInventory().getId().equals(inventoryId));
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return "redirect:/cart";

        double total = 0;
        for (OrderItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.UNPAID);
        order.setTotalAmount(total);

        Order savedOrder = orderService.saveOrder(order);

        for (OrderItem item : cart) {
            item.setOrder(savedOrder);
            orderItemService.saveOrderItem(item);

            Inventory inv = item.getInventory();
            inv.setQuantity(inv.getQuantity() - item.getQuantity());
            if (inv.getQuantity() <= 0) inv.setAvailable(false);
            inventoryService.addItem(inv);
        }

        session.removeAttribute("cart");

        Invoice invoice = invoiceService.generateInvoice(savedOrder);

        return "redirect:/invoice.html?id=" + invoice.getId();

    }

}
