package com.costadelsur.api.repo;

import com.costadelsur.api.model.MenuItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends IGenericRepo<MenuItem, Long> {
    
    // Encontrar todos los items publicados
    List<MenuItem> findByPublishedTrue();
    
    // Encontrar items disponibles (publicados y con stock > 0)
    @Query("SELECT m FROM MenuItem m WHERE m.published = true AND m.stock > 0")
    List<MenuItem> findAvailableItems();
    
    // Encontrar por categoría
    List<MenuItem> findByCategory(String category);
    
    // Encontrar por categoría y publicados
    List<MenuItem> findByCategoryAndPublishedTrue(String category);

    List<MenuItem> findByDiscountId(Long discountId);
}