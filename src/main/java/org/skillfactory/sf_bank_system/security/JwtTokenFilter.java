package org.skillfactory.sf_bank_system.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.entity.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                token = token.substring("Bearer ".length());
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthTokenFromJwt(token));
            }
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "TokenExpiredException");
        } catch (JWTDecodeException e) {
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, "JWTDecodeException");
        } catch (JWTVerificationException e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWTVerificationException");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String errorMessage) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), ErrorDto.builder()
                .message(errorMessage)
                .methodError(this.getFilterName())
                .classError(this.getClass().getName())
                .build());
    }

}
