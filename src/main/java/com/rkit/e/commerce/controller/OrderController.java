package com.rkit.e.commerce.controller;

import com.rkit.e.commerce.dto.OrderDTO;
import com.rkit.e.commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public OrderDTO placeOrder() {
        return orderService.placeOrder();
    }

    @GetMapping
    public List<OrderDTO> getUserOrders() {
        return orderService.getUserOrders();
    }

    @PatchMapping("/{orderId}/status")
    public OrderDTO updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String newStatus) {
        return orderService.updateOrderStatus(orderId, newStatus);
    }
}
