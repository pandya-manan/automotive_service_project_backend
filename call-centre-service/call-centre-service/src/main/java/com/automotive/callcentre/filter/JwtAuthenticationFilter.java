package com.automotive.callcentre.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.automotive.callcentre.entity.Role;
import com.automotive.callcentre.util.JwtTokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        
        // Skip filter for Swagger/OpenAPI endpoints
        if (requestPath.startsWith("/docs") || requestPath.startsWith("/swagger") || 
            requestPath.startsWith("/v3/api-docs") || requestPath.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            response.setContentType("application/json");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        
        try {
            // Validate token
            if (!jwtTokenUtil.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
                response.setContentType("application/json");
                return;
            }

            // Check role - only CALL_CENTRE_AGENT role allowed
            Role userRole = jwtTokenUtil.getRoleFromToken(token);
            if (userRole != Role.CALL_CENTRE_AGENT) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"error\":\"Access denied. CALL_CENTRE_AGENT role required.\"}");
                response.setContentType("application/json");
                return;
            }

            // Set user info in request attributes for use in controllers
            request.setAttribute("userId", jwtTokenUtil.getUserIdFromToken(token));
            request.setAttribute("userEmail", jwtTokenUtil.getUsernameFromToken(token));
            request.setAttribute("userRole", userRole);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid token: " + e.getMessage() + "\"}");
            response.setContentType("application/json");
        }
    }
}

