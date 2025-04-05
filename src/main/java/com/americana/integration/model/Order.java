package com.americana.integration.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private String id;
    private String status;
    private String customerId;
    private String restaurantId;
    private String deliveryDriverId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> items;
    private double totalAmount;
    private String deliveryAddress;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
}

@Data
class OrderItem {
    private String id;
    private String productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String notes;
} 