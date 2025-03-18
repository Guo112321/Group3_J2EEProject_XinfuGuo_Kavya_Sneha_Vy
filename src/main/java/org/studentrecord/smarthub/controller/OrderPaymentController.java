package org.studentrecord.smarthub.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.studentrecord.smarthub.model.Invoice;
import org.studentrecord.smarthub.model.Order;
import org.studentrecord.smarthub.model.OrderStatus;
import org.studentrecord.smarthub.service.InvoiceService;
import org.studentrecord.smarthub.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderPaymentController {

    private final OrderService orderService;
    private final InvoiceService invoiceService;

    public OrderPaymentController(OrderService orderService, InvoiceService invoiceService) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
    }

    @PostMapping("/{orderId}/pay")
    public String payForOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "Order not found";
        }
        if (order.getStatus() != OrderStatus.UNPAID) {
            return "Status is not UNPAID";
        }

        order.setStatus(OrderStatus.PAID);
        orderService.saveOrder(order);

        Invoice invoice = invoiceService.generateInvoice(order);

        return "Payment success! invoiceId = " + invoice.getId();
    }
}


