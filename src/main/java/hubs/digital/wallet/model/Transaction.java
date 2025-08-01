package hubs.digital.wallet.model;

import hubs.digital.wallet.common.OppositePartyType;
import hubs.digital.wallet.common.TransactionStatus;
import hubs.digital.wallet.common.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
@Getter
@Setter
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;

    private Long walletId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String oppositeParty;

    @Enumerated(EnumType.STRING)
    private OppositePartyType oppositePartType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}
