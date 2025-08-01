package hubs.digital.wallet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@Getter
@Setter
public class WalletConfig {
    @Value("${hubs.config.max-amount.to-approve:500}")
    private BigDecimal maxAmountToApprove;

    @Value("${hubs.jwt.secret}")
    private String jwtSecret;

    @Value("${hubs.jwt.expiration:2}")
    private Long jwxExpiration;
}