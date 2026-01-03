package com.costadelsur.api.controller;

import com.costadelsur.api.model.User;
import com.costadelsur.api.repo.IUserRepo;
import com.costadelsur.api.security.JwtRequest;
import com.costadelsur.api.security.JwtResponse;
import com.costadelsur.api.security.JwtTokenUtil;
import com.costadelsur.api.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final IUserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest req) {

        try {
            authenticate(req.getUsername(), req.getPassword());

            UserDetails userDetails =
                    jwtUserDetailsService.loadUserByUsername(req.getUsername());

            String token = jwtTokenUtil.generateToken(userDetails);

            User user = userRepo.findOneByUsername(req.getUsername());

            List<String> roles = user.getRoles()
                    .stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(
                    new JwtResponse(token, user.getUsername(), roles)
            );

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS");
        }
    }
}
