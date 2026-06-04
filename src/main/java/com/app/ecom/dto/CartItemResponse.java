package com.app.ecom.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemResponse {
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
