package com.costadelsur.api.controller;

import com.costadelsur.api.dto.MenuItemDTO;
import com.costadelsur.api.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MenuItemController {
    
    private final MenuItemService service;
    
    // Obtener todos los items
    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> findAll() {
        List<MenuItemDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }
    
    // Obtener item por ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> findById(@PathVariable("id") Long id) {
        MenuItemDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }
    
    // Obtener items por categoría
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItemDTO>> findByCategory(@PathVariable("category") String category) {
        List<MenuItemDTO> list = service.findByCategory(category);
        return ResponseEntity.ok(list);
    }
    
    // Obtener items disponibles (publicados y con stock > 0)
    @GetMapping("/available")
    public ResponseEntity<List<MenuItemDTO>> findAvailableItems() {
        List<MenuItemDTO> list = service.findAvailableItems();
        return ResponseEntity.ok(list);
    }
    
    // Obtener items publicados
    @GetMapping("/published")
    public ResponseEntity<List<MenuItemDTO>> findPublishedItems() {
        List<MenuItemDTO> list = service.findPublishedItems();
        return ResponseEntity.ok(list);
    }
    
    // Crear nuevo item
    @PostMapping
    public ResponseEntity<MenuItemDTO> save(@Valid @RequestBody MenuItemDTO dto) {
        MenuItemDTO savedDto = service.save(dto);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDto.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(savedDto);
    }
    
    // Actualizar item completo
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> update(@PathVariable("id") Long id,
                                              @Valid @RequestBody MenuItemDTO dto) {
        MenuItemDTO updatedDto = service.update(dto, id);
        return ResponseEntity.ok(updatedDto);
    }
    
    // Actualizar solo el stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<MenuItemDTO> updateStock(@PathVariable("id") Long id,
                                                   @RequestParam Integer stock) {
        MenuItemDTO updatedDto = service.updateStock(id, stock);
        return ResponseEntity.ok(updatedDto);
    }
    
    // Eliminar item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Paginación
    @GetMapping("/pageable")
    public ResponseEntity<Page<MenuItemDTO>> listPage(Pageable pageable) {
        Page<MenuItemDTO> page = service.listPage(pageable);
        return ResponseEntity.ok(page);
    }
}