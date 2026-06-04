package com.app.ecom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ecom.model.CartItem;
import com.app.ecom.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    
    //endpoint to add item to cart
    @PostMapping
    public ResponseEntity<String> addToCart(
        @RequestHeader("X-User-ID") String userId,
        @RequestBody CartItem request) 
        {
            if(cartService.addToCart(userId, request)){
                return ResponseEntity.status(201).body("Item added to cart successfully");
            }
            return ResponseEntity.status(400).body("Failed to add item to cart");
        }

    //endpoint to remove item from cart
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
        @RequestHeader("X-User-ID") String userId,
        @PathVariable Long productId) 
    {
        boolean result = cartService.deleteItemFromCart(userId, productId);
        if (result) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }
    

    //endpoint to get all items from cart
    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getItemsFromCart(
        @RequestHeader("X-User-ID") String userId) 
    {
        return ResponseEntity.status(200).body(cartService.getCartItemsForUser(userId));
    }

}
