package com.costadelsur.api.service;

import com.costadelsur.api.dto.DiscountRequestDTO;
import com.costadelsur.api.model.Discount;
import java.util.List;

public interface DiscountService {
    Discount createDiscount(DiscountRequestDTO dto);
    List<Discount> getAllDiscounts();
    void deleteDiscount(Long id);
}