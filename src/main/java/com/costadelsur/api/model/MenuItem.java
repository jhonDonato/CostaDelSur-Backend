package com.costadelsur.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(length = 500)
    private String image;

    @Column(nullable = false)
    private Integer stock = 0;
    
    @Column(nullable = false, length = 50)
    private String category; // 'Entradas', 'Platos Fuertes', 'Bebidas', 'Postres', 'Platos a la Carta'
    
    @Column(nullable = false)
    private Boolean published = true;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
}
