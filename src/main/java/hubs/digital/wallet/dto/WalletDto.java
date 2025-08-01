package hubs.digital.wallet.dto;

import hubs.digital.wallet.common.Currency;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private Long _id;

    private String walletName;

    private Currency currency;

    private boolean shoppingActive;

    private boolean withdrawActive;

    private BigDecimal balance;

    private BigDecimal usableBalance;
}