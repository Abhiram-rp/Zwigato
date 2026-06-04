package com.app.ecom.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.app.ecom.model.OrderStatus;

import lombok.Data;

@Data
public class OrderResponse {

    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items; 
    private LocalDateTime createdAt;
}
