package com.costadelsur.api.service;

import com.costadelsur.api.dto.MenuItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuItemService {
    
    // CRUD básico
    List<MenuItemDTO> findAll();
    MenuItemDTO findById(Integer id);
    MenuItemDTO save(MenuItemDTO dto);
    MenuItemDTO update(MenuItemDTO dto, Integer id);
    void delete(Integer id);
    
    // Métodos específicos
    List<MenuItemDTO> findByCategory(String category);
    List<MenuItemDTO> findAvailableItems();
    List<MenuItemDTO> findPublishedItems();
    MenuItemDTO updateStock(Integer id, Integer newStock);
    
    // Paginación
    Page<MenuItemDTO> listPage(Pageable pageable);
}