package hubs.digital.wallet.api.controller;

import hubs.digital.wallet.api.request.LoginRequest;
import hubs.digital.wallet.config.WalletConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

import static hubs.digital.wallet.common.Uris.Api_Prefix;

@Tag(
        name = "Login Simulator",
        description = "API to simulate a login for either an EMPLOYEE or a CUSTOMER"
)
@RestController
@RequestMapping(Api_Prefix)
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final WalletConfig config;

    @Operation(
            summary = "Simulate a login request to receive a JWT token",
            description = ""
    )
    @PostMapping("/simulate-login")
    public ResponseEntity<String> simulateLogin(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        final SecretKey KEY = Keys.hmacShaKeyFor(config.getJwtSecret().getBytes());
        return ResponseEntity.ok(Jwts.builder()
                .subject(loginRequest.getUserName())
                .claim("role", loginRequest.getUserRole())
                .claim("customerId", loginRequest.getCustomerId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Duration.ofSeconds(loginRequest.getJwtExpiryInSeconds()).toMillis()))
                .signWith(KEY, Jwts.SIG.HS256)
                .compact());
    }
}