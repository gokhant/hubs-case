package hubs.digital.wallet.api.request;

import hubs.digital.wallet.common.Currency;
import hubs.digital.wallet.common.OppositePartyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DepositRequest {
    @Schema(description = "Amount to deposit", example = "999.48", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "amount can not be null")
    private BigDecimal amount;

    @Schema(description = "Currency of the wallet", requiredMode = Schema.RequiredMode.REQUIRED, examples = {"TRY", "USD", "EUR"})
    @NotNull(message = "Currency can not be null")
    private Currency currency;

    @Schema(description = "Source of the deposit, can be an IBAN or paymentId", example = "LU243368736344FC5842", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "source can not be null")
    private String source;

    @Schema(description = "Type of the source; can be an IBAN or PAYMENT", examples = {"IBAN", "PAYMENT"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "sourceType can not be null")
    private OppositePartyType sourceType;
}