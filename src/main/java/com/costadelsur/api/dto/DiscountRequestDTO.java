package com.costadelsur.api.dto;

import com.costadelsur.api.model.enums.DiscountModality;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DiscountRequestDTO {
    private String description;
    private Double percentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DiscountModality modality;

    private List<Long> menuItemIds;

    private MenuItemDTO newPromoItem;

}