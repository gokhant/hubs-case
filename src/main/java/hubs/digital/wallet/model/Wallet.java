package hubs.digital.wallet.model;

import hubs.digital.wallet.common.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallet")
@Getter
@Setter
@ToString
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;

    private Long customerId;

    private String walletName;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private boolean shoppingActive;

    private boolean withdrawActive;

    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal usableBalance = BigDecimal.ZERO;

    public void increaseBalance(BigDecimal increase) {
        balance = balance.add(increase);
    }

    public void increaseUsableBalance(BigDecimal increase) {
        usableBalance = usableBalance.add(increase);
    }
}