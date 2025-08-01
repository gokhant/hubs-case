package hubs.digital.wallet.api.request;

import hubs.digital.wallet.common.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WalletCreateRequest {
    @Schema(description = "Name of the wallet", example = "My TRY Wallet", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "walletName can not be null")
    private String walletName;

    @Schema(description = "Currency of the wallet", requiredMode = Schema.RequiredMode.REQUIRED, examples = {"TRY", "USD", "EUR"})
    @NotNull(message = "Currency can not be null")
    private Currency currency;

    @Schema(description = "Shopping enabled for this wallet", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "shoppingActive can not be null")
    private boolean shoppingActive;

    @Schema(description = "Withdraw enabled for this wallet", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "withdrawActive can not be null")
    private boolean withdrawActive;
}