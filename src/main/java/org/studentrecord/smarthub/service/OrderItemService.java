package org.studentrecord.smarthub.service;

import org.springframework.stereotype.Service;
import org.studentrecord.smarthub.model.OrderItem;
import org.studentrecord.smarthub.repository.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems) {
        return orderItemRepository.saveAll(orderItems);
    }

    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }

    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id).orElse(null);
    }
}
