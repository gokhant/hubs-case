package hubs.digital.wallet.api.request;

import hubs.digital.wallet.common.Currency;
import hubs.digital.wallet.common.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginRequest {
    @Schema(description = "user name", requiredMode = Schema.RequiredMode.REQUIRED, example = "gokhan")
    @NotNull(message = "userName can not be null")
    private String userName;

    @Schema(description = "CustomerId, it would better be an existing one, not important when userRole is EMPLOYEE", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "userName can not be null")
    private Long customerId;

    @Schema(description = "Role of the user", requiredMode = Schema.RequiredMode.REQUIRED, examples = {"CUSTOMER", "EMPLOYEE"})
    @NotNull(message = "userRole can not be null")
    private UserRole userRole;

    @Schema(description = "Duration in seconds when the created JWT will expire", requiredMode = Schema.RequiredMode.REQUIRED, example="7200")
    @NotNull(message = "jwtExpiryInSeconds can not be null")
    private Long jwtExpiryInSeconds;
}