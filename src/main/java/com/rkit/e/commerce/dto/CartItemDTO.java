package com.rkit.e.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class CartItemDTO {

    private String productName;
    private BigDecimal productPrice;
    private int quantity;
    private BigDecimal totalPrice;
}
