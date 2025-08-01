package hubs.digital.wallet.api.security;

import hubs.digital.wallet.common.UserRole;
import hubs.digital.wallet.config.WalletConfig;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final WalletConfig config;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains("simulate-login") || request.getRequestURI().contains("swagger") || request.getRequestURI().contains("api-docs")
                || request.getRequestURI().contains("h2-console")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Jwt<?, ?> parsedJwt;
                try {
                    parsedJwt = Jwts.parser()
                            .verifyWith(Keys.hmacShaKeyFor(config.getJwtSecret().getBytes()))
                            .build().parseSignedClaims(token);
                } catch (ClaimJwtException cje) {
                    log.warn("jwt failed", cje);
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), cje.getMessage());
                    return;
                }
                Claims claims = (Claims) parsedJwt.getPayload();
                String userName = claims.getSubject();
                UserRole role = UserRole.from(claims.get("role", String.class));
                Long customerId = claims.get("customerId", Long.class);

                if (Objects.isNull(userName) || Objects.isNull(role) || Objects.isNull(customerId)) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "invalid/missing token claims");
                    return;
                }
                AuthenticatedUser user = new AuthenticatedUser(userName, role, customerId);
                SecurityContext.setUser(user);
                filterChain.doFilter(request, response);
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "missing Authorization header");
            }
        }
    }
}
