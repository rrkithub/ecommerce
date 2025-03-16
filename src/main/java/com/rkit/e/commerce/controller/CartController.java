package com.rkit.e.commerce.controller;

import com.rkit.e.commerce.dto.CartDTO;
import com.rkit.e.commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public CartDTO getCart() {
        return cartService.getCartForLoggedInUser();
    }

    @PostMapping("/add")
    public CartDTO addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        return cartService.addToCart(productId, quantity);
    }

    @DeleteMapping("/remove")
    public CartDTO removeFromCart(@RequestParam Long productId) {
        return cartService.removeFromCart(productId);
    }

    @DeleteMapping("/removeOne")
    public CartDTO removeOneQuantity(@RequestParam Long productId) {
        return cartService.removeOneQuantity(productId);
    }


    @DeleteMapping("/clear")
    public void clearCart() {
        cartService.clearCart();
    }
}

