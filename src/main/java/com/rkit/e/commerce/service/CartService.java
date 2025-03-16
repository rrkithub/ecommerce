package com.rkit.e.commerce.service;

import com.rkit.e.commerce.dto.CartDTO;
import com.rkit.e.commerce.dto.CartItemDTO;
import com.rkit.e.commerce.entity.Cart;
import com.rkit.e.commerce.entity.CartItem;
import com.rkit.e.commerce.entity.Product;
import com.rkit.e.commerce.entity.User;
import com.rkit.e.commerce.repository.CartItemRepository;
import com.rkit.e.commerce.repository.CartRepository;
import com.rkit.e.commerce.repository.ProductRepository;
import com.rkit.e.commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    public CartDTO getCartForLoggedInUser() {
        String username = getCurrentUsername();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        List<CartItemDTO> cartItemDtos = cart.getItems().stream()
                .map(item -> new CartItemDTO(item.getProduct().getName(), item.getProduct().getPrice(), item.getQuantity(), item.getTotalPrice()))
                .collect(Collectors.toList());

        return new CartDTO(username, cartItemDtos);
    }


    public CartDTO addToCart(Long productId, int quantity) {
        String username = getCurrentUsername();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());

        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.calculateTotalPrice();

        if (!cart.getItems().contains(cartItem)) {
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);

        return getCartForLoggedInUser();
    }

    public CartDTO removeFromCart(Long productId) {
        String username = getCurrentUsername();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);

        return getCartForLoggedInUser();
    }


    public void clearCart() {
        String username = getCurrentUsername();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public CartDTO removeOneQuantity(Long productId) {
        String username = getCurrentUsername();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Optional<CartItem> cartItemOptional = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();

            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItem.calculateTotalPrice();
            } else {
                cart.getItems().remove(cartItem);
            }

            cartRepository.save(cart);
        }

        return getCartForLoggedInUser();
    }



    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new RuntimeException("User not authenticated");
    }
}

