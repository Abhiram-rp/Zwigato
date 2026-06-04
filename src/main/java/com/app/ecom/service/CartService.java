package com.app.ecom.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
   
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    //Service method to add item to cart
    public boolean addToCart(String userId, CartItemRequest request) {

        //Look for product
        Optional<Product> productOpt = productRepository.findById(request.getProductId());

        if(productOpt.isEmpty()) {
            return false;
        }

        Product product = productOpt.get();
        if(product.getStockQuantity() < request.getQuantity()) {
            return false;
        }

        //Look for user
        Optional<User> userOpt = userRepository.findById(Long.parseLong(userId));

        if(userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        //Now, user and product both exists, so we can add to cart
        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);
        if(existingCartItem != null) {
            //Cart item already exists, so update quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }

        else{
            //Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    //Service method to delete item from cart
    public boolean deleteItemFromCart(String userId, Long productId) {
        //Look for user
        Optional<User> userOpt = userRepository.findById(Long.parseLong(userId));
        if(userOpt.isEmpty()) {
            return false;
        }

        //Look for product
        Optional<Product> productOpt = productRepository.findById(productId);
        if(productOpt.isEmpty()) {
            return false;
        }

        //Check if both user and product exists, then delete cart item
        if(userOpt.isPresent() && productOpt.isPresent()) {
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true;
        }
        
        return false;
    }

    //Service method to get all cart items for a user
    public List<CartItemResponse> getCartItemsForUser(String userId) {
        
        //Look for user
        Optional<User> userOpt = userRepository.findById(Long.parseLong(userId));
        if(userOpt.isEmpty()) {
            return List.of();
        }

        //Get cart items for user
        List<CartItem> cartItems = cartItemRepository.findByUser(userOpt.get());
        return cartItems.stream()
                        .map(this::mapToCartItemResponse)
                        .collect(Collectors.toList());
    }

    //DTO Mapping method

    public CartItemResponse mapToCartItemResponse(CartItem cartItem) {
            CartItemResponse response = new CartItemResponse();
            response.setProductName(cartItem.getProduct().getName());
            response.setQuantity(cartItem.getQuantity());
            response.setPrice(cartItem.getPrice());
            return response;
        }

}
