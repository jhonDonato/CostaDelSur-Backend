package com.costadelsur.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        System.out.println("DEBUG [Backend]: Request a " + request.getRequestURI() + " - Header Auth: " + (header != null ? "Presente" : "Nulo"));

        String username = null;
        String jwtToken = null;

        if(header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                System.out.println("DEBUG [Backend]: Token decodificado para usuario: " + username);
            } catch (Exception e) {
                System.err.println("DEBUG [Backend]: Error decodificando token: " + e.getMessage());
                request.setAttribute("msg", e.getMessage());
            }
        }

        if(username != null && jwtToken != null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            boolean isValid = jwtTokenUtil.validateToken(jwtToken, userDetails);
            System.out.println("DEBUG [Backend]: ¿Token válido?: " + isValid);

            if(isValid) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("DEBUG [Backend]: Autenticación establecida en el contexto.");
            }
        }

        filterChain.doFilter(request, response);
    }
}
