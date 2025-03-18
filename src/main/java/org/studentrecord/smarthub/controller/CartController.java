package org.studentrecord.smarthub.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.*;
import org.studentrecord.smarthub.service.InventoryService;
import org.studentrecord.smarthub.service.OrderItemService;
import org.studentrecord.smarthub.service.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public CartController(InventoryService inventoryService,
                          OrderService orderService,
                          OrderItemService orderItemService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("inventoryId") Long inventoryId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session) {

        Inventory inventory = inventoryService.getItemById(inventoryId);
        if (inventory == null || quantity <= 0) {
            return "item invalid!";
        }

        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        boolean found = false;
        for (OrderItem item : cart) {
            if (item.getInventory().getId().equals(inventoryId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setInventory(inventory);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(inventory.getPrice());
            cart.add(orderItem);
        }

        session.setAttribute("cart", cart);
        return "added to cart!";
    }

    @GetMapping
    public List<OrderItem> viewCart(HttpSession session) {
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return cart;
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "please login first!";
        }

        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "cart is empty!";
        }

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

        for (OrderItem ci : cart) {
            ci.setOrder(savedOrder);
            orderItemService.saveOrderItem(ci);

            Inventory inv = ci.getInventory();
            inv.setQuantity(inv.getQuantity() - ci.getQuantity());

            if (inv.getQuantity() <= 0) {
                inv.setAvailable(false);
            }

            inventoryService.addItem(inv);
        }

        session.removeAttribute("cart");

        return String.valueOf(savedOrder.getId());
    }

    @GetMapping("/order/{orderId}")
    public Order getOrderInfo(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }
}
