package com.gestion.materiel.security;


import com.gestion.materiel.service.impl.AgentServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AgentServiceImpl agentServiceImpl;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, @Lazy AgentServiceImpl agentServiceImpl) {
        this.jwtUtil = jwtUtil;
        this.agentServiceImpl = agentServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String cin = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                cin = jwtUtil.extractCin(token);
                System.out.println("CIN is " + cin);
            } catch (ExpiredJwtException | SignatureException e) {
                System.out.println("Invalid JWT Token: " + e.getMessage());
            }
        }

        if (cin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = agentServiceImpl.loadUserByUsername(cin);

            if (jwtUtil.validateToken(token) && cin.equals(userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
