package com.app.ecom.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Order;
import com.app.ecom.model.OrderItem;
import com.app.ecom.model.OrderStatus;
import com.app.ecom.model.User;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        //Check if items are there in cart
        List<CartItem> cartItems = cartService.getCartItemsForUser(userId);
        if(cartItems.isEmpty()){
            Optional.empty();
        }

        //Check for user
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){
            Optional.empty();
        }
        User user = userOptional.get();

        //Calculate the total price
        BigDecimal totalPrice = cartItems.stream()
                                        .map(CartItem::getPrice)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                                                .map(item -> new OrderItem(
                                                    null,
                                                    item.getProduct(),
                                                    item.getQuantity(),
                                                    item.getPrice(),
                                                    order
                                                ))
                                                .toList();
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        //Clear the cart
        // cartService.clearCart();
    }

}
