package hubs.digital.wallet.service.impl;

import hubs.digital.wallet.api.request.DepositRequest;
import hubs.digital.wallet.api.request.WalletCreateRequest;
import hubs.digital.wallet.common.Currency;
import hubs.digital.wallet.common.OppositePartyType;
import hubs.digital.wallet.exception.ResourceNotFound;
import hubs.digital.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class WalletServiceComponentTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void createWallet_shouldPersistAndReturnId() {
        WalletCreateRequest request = WalletCreateRequest.builder().walletName("wallet-01").shoppingActive(true)
                        .withdrawActive(true).currency(Currency.TRY).build();
        Long customerId = -1L;
        Long walletId = walletService.createWallet(customerId, request);

        Assertions.assertNotNull(walletId);
        Assertions.assertTrue(walletRepository.findById(walletId).isPresent());
    }

    @Test
    void makeDeposit_shouldThrowResourceNotFound_whenWalletDoesNotExist() {
        Long customerId = -1L;
        Long walletId = 4545345L;
        DepositRequest depositRequest = DepositRequest.builder().amount(BigDecimal.TEN).source("3114124124123412").sourceType(OppositePartyType.IBAN).currency(Currency.TRY).build();
        Assertions.assertThrows(ResourceNotFound.class, () -> {
            walletService.makeDeposit(customerId, walletId, depositRequest);
        });
    }
}