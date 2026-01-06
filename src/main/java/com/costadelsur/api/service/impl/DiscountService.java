package com.costadelsur.api.service.impl;

import com.costadelsur.api.dto.DiscountRequestDTO;
import com.costadelsur.api.model.Discount;
import com.costadelsur.api.model.MenuItem;
import com.costadelsur.api.repo.DiscountRepository;
import com.costadelsur.api.repo.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Transactional
    public Discount createDiscount(DiscountRequestDTO dto) {
        Discount discount = new Discount();
        discount.setDescription(dto.getDescription());
        discount.setPercentage(dto.getPercentage());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());
        discount.setModality(dto.getModality());
        discount.setActive(true);

        Discount savedDiscount = discountRepository.save(discount);

        if (dto.getMenuItemIds() != null && !dto.getMenuItemIds().isEmpty()) {
            List<MenuItem> items = menuItemRepository.findAllById(dto.getMenuItemIds());
            for (MenuItem item : items) {
                item.setDiscount(savedDiscount);
            }
            menuItemRepository.saveAll(items);
        }

        return savedDiscount;
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Transactional
    public void deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));

        // Desvincular platos antes de borrar la oferta
        List<MenuItem> itemsWithDiscount = menuItemRepository.findAll();
        for(MenuItem item : itemsWithDiscount) {
            if(item.getDiscount() != null && item.getDiscount().getId().equals(id)){
                item.setDiscount(null);
            }
        }
        menuItemRepository.saveAll(itemsWithDiscount);

        discountRepository.deleteById(id);
    }
}
