package com.costadelsur.api.config;

import com.costadelsur.api.model.Role;
import com.costadelsur.api.model.User;
import com.costadelsur.api.repo.RolRepository;
import com.costadelsur.api.repo.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RolRepository roleRepo;
    private final IUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {

        // =========================
        // CREAR ROLES SI NO EXISTEN
        // =========================
        Role adminRole = createRoleIfNotExists("ADMIN", "Rol para administradores");
        Role meseroRole = createRoleIfNotExists("MESERO", "Rol para meseros");
        Role cocinaRole = createRoleIfNotExists("COCINA", "Rol para cocina");

        // =========================
        // CREAR USUARIOS POR DEFECTO
        // =========================
        createUserIfNotExists("maria@gmail.com", "123456", adminRole);
        createUserIfNotExists("jhon@gmail.com", "123456", meseroRole);
        createUserIfNotExists("camila@gmail.com", "123456", cocinaRole);
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private Role createRoleIfNotExists(String name, String description) {
        return roleRepo.findAll().stream()
                .filter(role -> role.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setDescription(description);
                    roleRepo.save(role);
                    System.out.println("✅ Rol creado: " + name);
                    return role;
                });
    }

    private void createUserIfNotExists(String email, String password, Role role) {
        if (userRepo.findOneByUsername(email) == null) {
            User user = new User();
            user.setUsername(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
            user.setRoles(List.of(role));

            userRepo.save(user);
            System.out.println("✅ Usuario creado: " + email + " | Rol: " + role.getName());
        }
    }
}
