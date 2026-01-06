package com.costadelsur.api.service.impl;

import com.costadelsur.api.dto.MenuItemDTO;
import com.costadelsur.api.exception.ModelNotFoundException;
import com.costadelsur.api.model.MenuItem;
import com.costadelsur.api.repo.MenuItemRepository;
import com.costadelsur.api.service.MenuItemService;
import com.costadelsur.api.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    
    private final MenuItemRepository repository;
    private final MapperUtil mapperUtil;
    
    @Override
    public List<MenuItemDTO> findAll() {
        List<MenuItem> items = repository.findAll();
        return mapperUtil.mapList(items, MenuItemDTO.class);
    }
    
    @Override
    public MenuItemDTO findById(Long id) {
        MenuItem item = repository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException("Item del menú no encontrado con ID: " + id));
        return mapperUtil.map(item, MenuItemDTO.class);
    }
    
    @Override
    @Transactional
    public MenuItemDTO save(MenuItemDTO dto) {
        MenuItem item = mapperUtil.map(dto, MenuItem.class);
        item = repository.save(item);
        return mapperUtil.map(item, MenuItemDTO.class);
    }
    
    @Override
    @Transactional
    public MenuItemDTO update(MenuItemDTO dto, Long id) {
        // Verificar que existe
        MenuItem existingItem = repository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException("Item del menú no encontrado con ID: " + id));
        
        // Actualizar propiedades
        existingItem.setName(dto.getName());
        existingItem.setDescription(dto.getDescription());
        existingItem.setPrice(dto.getPrice());
        existingItem.setImage(dto.getImage());
        existingItem.setStock(dto.getStock());
        existingItem.setCategory(dto.getCategory());
        existingItem.setPublished(dto.getPublished());
        
        // Guardar cambios
        existingItem = repository.save(existingItem);
        
        return mapperUtil.map(existingItem, MenuItemDTO.class);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ModelNotFoundException("Item del menú no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }
    
    @Override
    public List<MenuItemDTO> findByCategory(String category) {
        List<MenuItem> items = repository.findByCategory(category);
        return mapperUtil.mapList(items, MenuItemDTO.class);
    }
    
    @Override
    public List<MenuItemDTO> findAvailableItems() {
        List<MenuItem> items = repository.findAvailableItems();
        return mapperUtil.mapList(items, MenuItemDTO.class);
    }
    
    @Override
    public List<MenuItemDTO> findPublishedItems() {
        List<MenuItem> items = repository.findByPublishedTrue();
        return mapperUtil.mapList(items, MenuItemDTO.class);
    }
    
    @Override
    @Transactional
    public MenuItemDTO updateStock(Long id, Integer newStock) {
        MenuItem item = repository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException("Item del menú no encontrado con ID: " + id));
        
        if (newStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        item.setStock(newStock);
        item = repository.save(item);
        
        return mapperUtil.map(item, MenuItemDTO.class);
    }
    
    @Override
    public Page<MenuItemDTO> listPage(Pageable pageable) {
        Page<MenuItem> page = repository.findAll(pageable);
        return page.map(item -> mapperUtil.map(item, MenuItemDTO.class));
    }
}