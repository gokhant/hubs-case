package hubs.digital.wallet.dto;

import hubs.digital.wallet.common.OppositePartyType;
import hubs.digital.wallet.common.TransactionStatus;
import hubs.digital.wallet.common.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long _id;

    private Long walletId;

    private BigDecimal amount;

    private TransactionType type;

    private String oppositeParty;

    private OppositePartyType oppositePartType;

    private TransactionStatus status;
}