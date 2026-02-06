package com.costadelsur.api.service.impl;

import com.costadelsur.api.dto.DiscountRequestDTO;
import com.costadelsur.api.model.Discount;
import com.costadelsur.api.model.MenuItem;
import com.costadelsur.api.repo.DiscountRepository;
import com.costadelsur.api.repo.MenuItemRepository;
import com.costadelsur.api.service.DiscountService;
import com.costadelsur.api.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Override
    @Transactional
    public Discount createDiscount(DiscountRequestDTO dto) {
        // 1. Crear la Oferta base
        Discount discount = new Discount();
        discount.setDescription(dto.getDescription());
        discount.setPercentage(dto.getPercentage());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());
        discount.setModality(dto.getModality());
        discount.setActive(true);

        Discount savedDiscount = discountRepository.save(discount);

        // LÓGICA A: Aplicar a platos existentes
        if (dto.getMenuItemIds() != null && !dto.getMenuItemIds().isEmpty()) {
            List<MenuItem> items = menuItemRepository.findAllById(dto.getMenuItemIds());
            for (MenuItem item : items) {
                item.setDiscount(savedDiscount);
            }
            menuItemRepository.saveAll(items);
        }

        // LÓGICA B: Crear un plato NUEVO temporal (si el DTO lo incluye)
        // Nota: Asegúrate de que el campo 'newPromoItem' exista en tu DiscountRequestDTO
        /* if (dto.getNewPromoItem() != null) {
            MenuItem promoItem = mapperUtil.map(dto.getNewPromoItem(), MenuItem.class);
            promoItem.setIsPromo(true);
            promoItem.setPublished(true);
            promoItem.setDiscount(savedDiscount);
            menuItemRepository.save(promoItem);
        }
        */

        return savedDiscount;
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));

        // Desvincular platos y manejar platos temporales
        List<MenuItem> items = menuItemRepository.findByDiscountId(id);

        for (MenuItem item : items) {
            if (Boolean.TRUE.equals(item.getIsPromo())) {
                item.setPublished(false); // Ocultar si es temporal
            }
            item.setDiscount(null); // Quitar descuento
        }
        menuItemRepository.saveAll(items);

        discountRepository.delete(discount);
    }
}