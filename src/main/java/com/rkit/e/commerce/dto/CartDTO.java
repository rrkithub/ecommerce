package com.rkit.e.commerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class CartDTO {

    private String username;
    private List<CartItemDTO> items;
    private BigDecimal totalPrice;

    public CartDTO(String username, List<CartItemDTO> items) {
        this.username = username;
        this.items = items;
        this.totalPrice = items.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
