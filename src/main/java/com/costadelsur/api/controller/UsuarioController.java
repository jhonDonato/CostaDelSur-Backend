package com.costadelsur.api.controller;

import com.costadelsur.api.dto.UsuarioDTO;
import com.costadelsur.api.model.User;
import com.costadelsur.api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // ✅ Listar usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() throws Exception {
        List<User> usuarios = usuarioService.findAll();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(user -> mapper.map(user, UsuarioDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }

    // ✅ Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable("id") Integer id) {
        try {
            User user = usuarioService.findById(id);
            UsuarioDTO dto = mapper.map(user, UsuarioDTO.class);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Registrar usuario (SIN rol por defecto)
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioDTO dto) {
        try {
            // 1️⃣ Mapear DTO a entidad
            User user = mapper.map(dto, User.class);

            // 2️⃣ Encriptar contraseña
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

            // 3️⃣ Habilitar usuario
            user.setEnabled(true);

            // 4️⃣ Guardar usuario (sin roles)
            User creado = usuarioService.save(user);

            UsuarioDTO respuesta = mapper.map(creado, UsuarioDTO.class);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Modificar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> modificar(@PathVariable("id") Integer id,
                                       @Valid @RequestBody UsuarioDTO dto) {
        try {
            User existente = usuarioService.findById(id);

            existente.setUsername(dto.getUsername());

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                existente.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            existente.setEnabled(
                    dto.getEnabled() != null ? dto.getEnabled() : existente.getEnabled()
            );

            User actualizado = usuarioService.update(existente, id);
            UsuarioDTO respuesta = mapper.map(actualizado, UsuarioDTO.class);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Integer id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
