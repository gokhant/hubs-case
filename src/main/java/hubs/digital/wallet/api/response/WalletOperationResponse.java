package hubs.digital.wallet.api.response;

import hubs.digital.wallet.common.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WalletOperationResponse {
    private Long transactionId;
    private BigDecimal balance;
    private BigDecimal usableBalance;
    private TransactionStatus transactionStatus;
}
