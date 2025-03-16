package com.rkit.e.commerce.service;

import com.rkit.e.commerce.dto.OrderDTO;
import com.rkit.e.commerce.dto.OrderItemDTO;
import com.rkit.e.commerce.entity.*;
import com.rkit.e.commerce.repository.CartRepository;
import com.rkit.e.commerce.repository.OrderRepository;
import com.rkit.e.commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    public OrderDTO placeOrder() {
        String username = getCurrentUsername();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot place order. Cart is empty.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Pending");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(cartItem.getTotalPrice());
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

        cartService.clearCart();

        return new OrderDTO(
                order.getId(),
                username,
                orderItems.stream()
                        .map(item -> new OrderItemDTO(
                                item.getProduct().getName(),
                                item.getProduct().getPrice(),
                                item.getQuantity(),
                                item.getTotalPrice()))
                        .collect(Collectors.toList()),
                totalAmount,
                order.getStatus(),
                order.getOrderDate()
        );
    }

    public List<OrderDTO> getUserOrders() {
        String username = getCurrentUsername();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(order -> new OrderDTO(
                order.getId(),
                username,
                order.getItems().stream()
                        .map(item -> new OrderItemDTO(
                                item.getProduct().getName(),
                                item.getProduct().getPrice(),
                                item.getQuantity(),
                                item.getTotalPrice()))
                        .collect(Collectors.toList()),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate()
        )).collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        orderRepository.save(order);

        return new OrderDTO(
                order.getId(),
                order.getUser().getUserName(),
                order.getItems().stream()
                        .map(item -> new OrderItemDTO(
                                item.getProduct().getName(),
                                item.getProduct().getPrice(),
                                item.getQuantity(),
                                item.getTotalPrice()))
                        .collect(Collectors.toList()),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate()
        );
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new RuntimeException("User not authenticated");
    }
}
