package hubs.digital.wallet.api.controller;

import hubs.digital.wallet.api.security.CheckAccess;
import hubs.digital.wallet.common.TransactionStatus;
import hubs.digital.wallet.dto.TransactionDto;
import hubs.digital.wallet.service.impl.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
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
        name = "Transaction Management",
        description = "APIs for listing and modifying transactions. Access control is enforced based on user role (EMPLOYEE or CUSTOMER)"
)
@RestController
@RequestMapping(Api_Prefix)
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(
            summary = "List transactions done on a wallet",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(value = Transactions, produces = {MediaType.APPLICATION_JSON_VALUE})
    @CheckAccess(CheckAccess.AccessType.CUSTOMER_OWN_RESOURCE)
    public ResponseEntity<List<TransactionDto>> listTransactions(@Valid @PathVariable("customerId") Long customerId, @Valid @PathVariable("walletId") Long walletId, HttpServletRequest request) {
        log.info("REQUEST-{}-{} for {} - {}", Transactions, request.getMethod(), customerId, walletId);
        return ResponseEntity.ok(transactionService.listTransactions(customerId, walletId));
    }

    @Operation(
            summary = "Change the status of a transaction to a customer's wallet",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping(value = Transactions + "/{transactionId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @CheckAccess(CheckAccess.AccessType.EMPLOYEE_ONLY)
    public ResponseEntity<TransactionDto> updateStatus(@RequestBody @Valid TransactionStatus status,  @Valid @PathVariable("customerId") Long customerId, @Valid @PathVariable("walletId") Long walletId
            , @Valid @PathVariable Long transactionId, HttpServletRequest request) {
        log.info("REQUEST-{}-{} for {} - {} - {} : {}", Transactions, request.getMethod(), customerId, walletId, transactionId, status);
        return ResponseEntity.ok(transactionService.updateStatus(walletId, transactionId, status));
    }
}