package hubs.digital.wallet.api.controller;

import hubs.digital.wallet.api.request.DepositRequest;
import hubs.digital.wallet.api.request.WithdrawalRequest;
import hubs.digital.wallet.api.request.WalletCreateRequest;
import hubs.digital.wallet.api.response.WalletOperationResponse;
import hubs.digital.wallet.api.security.CheckAccess;
import hubs.digital.wallet.dto.WalletDto;
import hubs.digital.wallet.service.IWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hubs.digital.wallet.common.Uris.*;

@Tag(
    name = "Wallet Management",
    description = "APIs for creating and managing wallets. Access control is enforced based on user role EMPLOYEE or CUSTOMER. Customers with EMPLOYEE role can only process their own records whereas EMPLOYEE role users can process without any limitation."
)
@RestController
@RequestMapping(Api_Prefix)
@RequiredArgsConstructor
@Slf4j
public class WalletController {
    private final IWalletService walletService;

    @Operation(
            summary = "Create a wallet for a customer",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallet created successfully, returns walletId"),
            @ApiResponse(responseCode = "403", description = "Forbidden (role mismatch or unauthorized access)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (missing or invalid token)")
    })
    @PostMapping(value = Wallets, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @CheckAccess(CheckAccess.AccessType.CUSTOMER_OWN_RESOURCE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Long> createWallet(@RequestBody @Valid WalletCreateRequest walletCreateRequest, @Valid @PathVariable("customerId") Long customerId, HttpServletRequest request) {
        log.info("REQUEST-{}-{} for {}, {}", Wallets, request.getMethod(), customerId, walletCreateRequest);
        return ResponseEntity.ok(walletService.createWallet(customerId, walletCreateRequest));
    }

    @Operation(
            summary = "Gets list of wallets for a customer",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(value = Wallets, produces = {MediaType.APPLICATION_JSON_VALUE})
    @CheckAccess(CheckAccess.AccessType.CUSTOMER_OWN_RESOURCE)
    public ResponseEntity<List<WalletDto>> listWallets(@Valid @PathVariable("customerId") Long customerId, HttpServletRequest request) {
        log.info("REQUEST-{}-{} for {}", Wallets, request.getMethod(), customerId);
        return ResponseEntity.ok(walletService.listWallets(customerId));
    }

    @Operation(
            summary = "Deposit to a customer's wallet",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(value = Wallets + "/{walletId}" + Deposit, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @CheckAccess(CheckAccess.AccessType.CUSTOMER_OWN_RESOURCE)
    public ResponseEntity<WalletOperationResponse> makeDeposit(@RequestBody @Valid DepositRequest depositRequest, @Valid @PathVariable("customerId") Long customerId, @Valid @PathVariable("walletId") Long walletId, HttpServletRequest request) {
        log.info("REQUEST-{}-{} for {} - {} : {}", Deposit, request.getMethod(), customerId, walletId, depositRequest);
        return ResponseEntity.ok(walletService.makeDeposit(customerId, walletId, depositRequest));
    }

    @Operation(
            summary = "Withdraws from a customer's wallet",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(value = Wallets + "/{walletId}" + Withdrawal, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @CheckAccess(CheckAccess.AccessType.CUSTOMER_OWN_RESOURCE)
    public ResponseEntity<WalletOperationResponse> makeWithDraw(@RequestBody @Valid WithdrawalRequest withdrawalRequest, @Valid @PathVariable("customerId") Long customerId, @Valid @PathVariable("walletId") Long walletId, HttpServletRequest request) {
        log.info("REQUEST-{}-{} for {} - {} : {}", Withdrawal, request.getMethod(), customerId, walletId, withdrawalRequest);
        return ResponseEntity.ok(walletService.makeWithdraw(customerId, walletId, withdrawalRequest));
    }
}