package com.italohreis.medly.security.filter;

import com.italohreis.medly.security.config.JwtTokenProvider;
import com.italohreis.medly.security.service.JpaUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JpaUserDetailsService userDetailsService;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/auth/login",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/webjars/**",
            "/configuration/ui",
            "/configuration/security"
    );

    private static final List<String> PUBLIC_POST_PATHS = Arrays.asList(
            "/patients"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (isPublicPath(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = this.recoverToken(request);

        if (token != null) {
            try {
                String email = jwtTokenProvider.validateToken(token);
                UserDetails user = userDetailsService.loadUserByUsername(email);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    private boolean isPublicPath(String path, String method) {
        if ("POST".equals(method) && PUBLIC_POST_PATHS.stream().anyMatch(path::startsWith)) {
            return true;
        }

        return PUBLIC_PATHS.stream().anyMatch(publicPath ->
                path.equals(publicPath) || path.startsWith(publicPath + "/")
        );
    }
}