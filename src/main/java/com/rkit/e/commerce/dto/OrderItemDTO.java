package com.rkit.e.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
}

