package com.FirstProject.TinyURL.config;

import com.FirstProject.TinyURL.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        System.out.println("REQUEST URI = " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

//        System.out.println("AUTH HEADER = " + authHeader);

        if (authHeader!=null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                if(jwtService.isTokenValid(token)) {

                    String email = jwtService.extractEmail(token);

                    String role = jwtService.extractRole(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    AuthorityUtils.createAuthorityList("ROLE_"+role)
                            );

//                    System.out.println("JWT FOUND");

                    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                    System.out.println("EMAIL = " + email);
//                    System.out.println("ROLE = " + role);
//                    System.out.println("AUTHENTICATION SET");

                }
        }

        filterChain.doFilter(request, response);

    }
}
