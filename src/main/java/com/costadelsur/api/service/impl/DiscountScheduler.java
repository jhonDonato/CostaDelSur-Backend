package com.costadelsur.api.service.impl;

import com.costadelsur.api.model.Discount;
import com.costadelsur.api.model.MenuItem;
import com.costadelsur.api.repo.DiscountRepository;
import com.costadelsur.api.repo.MenuItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DiscountScheduler {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkExpiredDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> expired = discountRepository.findByActiveTrueAndEndDateBefore(now);

        for (Discount discount : expired) {
            List<MenuItem> items = menuItemRepository.findByDiscountId(discount.getId());
            for (MenuItem item : items) {
                if (Boolean.TRUE.equals(item.getIsPromo())) {
                    item.setPublished(false);
                }
                item.setDiscount(null);
            }
            menuItemRepository.saveAll(items);
            discount.setActive(false);
        }
        if (!expired.isEmpty()) discountRepository.saveAll(expired);
    }
}
