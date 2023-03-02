package com.microservices.orderservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "cart_id")
    private int cartId;
    @Column(name = "short_desc")
    private String shortDesc;
    @Column(name = "is_paid")
    private Boolean isPaid;
    @Column(name = "total_price")
    private Double price;
}
