package com.costadelsur.api.model;

import com.costadelsur.api.model.enums.DiscountModality;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description; // Ej: "Oferta Verano"

    @Column(nullable = false)
    private Double percentage; // Ej: 20.0

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private DiscountModality modality;

    private boolean active = true;
}
