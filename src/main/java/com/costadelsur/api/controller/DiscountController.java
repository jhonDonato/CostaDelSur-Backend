package com.costadelsur.api.controller;

import com.costadelsur.api.dto.DiscountRequestDTO;
import com.costadelsur.api.model.Discount;
import com.costadelsur.api.service.impl.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@CrossOrigin("*")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public List<Discount> getAll() {
        return discountService.getAllDiscounts();
    }

    @PostMapping
    public ResponseEntity<Discount> create(@RequestBody DiscountRequestDTO dto) {
        return ResponseEntity.ok(discountService.createDiscount(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
}
