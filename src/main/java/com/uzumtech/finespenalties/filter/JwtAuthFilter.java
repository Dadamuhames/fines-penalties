package com.uzumtech.finespenalties.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.dto.response.error.ErrorResponse;
import com.uzumtech.finespenalties.exception.JwtMalformedException;
import com.uzumtech.finespenalties.service.intr.token.JwtService;
import com.uzumtech.finespenalties.service.impl.auth.userdetails.UserDetailDispatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserDetailDispatcher detailsServiceDispatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);

        if (isAuthenticated) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);


        try {
            if (jwtService.isTokenExpired(jwt)) {
                filterChain.doFilter(request, response);
                throw new JwtMalformedException(ErrorCode.JWT_INVALID_CODE);
            }

            String username = jwtService.extractSubject(jwt);
            Role role = jwtService.extractRole(jwt);

            UserDetails userDetails = detailsServiceDispatcher.loadUserByLoginAndRole(username, role);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (JwtMalformedException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            var errorResponse = ErrorResponse.of(ex);

            objectMapper.writeValue(response.getWriter(), errorResponse);
            return;
        } catch (Exception ex) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            var errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVICE_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

            objectMapper.writeValue(response.getWriter(), errorResponse);

            return;
        }

        filterChain.doFilter(request, response);
    }
}
