package hubs.digital.wallet.service.impl;

import hubs.digital.wallet.api.request.WalletCreateRequest;
import hubs.digital.wallet.api.request.WithdrawalRequest;
import hubs.digital.wallet.common.Currency;
import hubs.digital.wallet.common.OppositePartyType;
import hubs.digital.wallet.config.WalletConfig;
import hubs.digital.wallet.exception.InsufficientBalance;
import hubs.digital.wallet.model.Wallet;
import hubs.digital.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletConfig config;

    @Test
    void createWallet_shouldReturnWalletId() {
        Wallet wallet = Wallet.builder().customerId(1L)._id(35L).walletName("testWallet").balance(BigDecimal.ZERO).withdrawActive(true).shoppingActive(true).currency(Currency.TRY).build();
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        Long createdWalletId = walletService.createWallet(1L, WalletCreateRequest.builder().build());
        Assertions.assertEquals(35L, createdWalletId);
    }


    @Test
    void makeWithdraw_shouldThrowResponseStatusExceptionWhenWalletIsNotWIthdrawEnabled() {
        Wallet wallet = Wallet.builder()
                .customerId(1L)._id(35L)
                .walletName("testWallet")
                .balance(BigDecimal.ZERO)
                .usableBalance(BigDecimal.ZERO)
                .withdrawActive(false).shoppingActive(true).currency(Currency.TRY).build();
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder().amount(BigDecimal.TEN).destination("42234234234").destinationType(OppositePartyType.IBAN)
                        .currency(Currency.TRY).build();
        when(walletRepository.findById(35L)).thenReturn(Optional.ofNullable(wallet));
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            walletService.makeWithdraw(wallet.getCustomerId(), wallet.get_id(), withdrawalRequest);
        });
    }

    @Test
    void makeWithdraw_shouldThrowInsufficientBalanceWhenWalletBalanceIsSmallerThenWithdrawal() {
        Wallet wallet = Wallet.builder()
                .customerId(1L)._id(35L)
                .walletName("testWallet")
                .balance(BigDecimal.ONE)
                .usableBalance(BigDecimal.ONE)
                .withdrawActive(true).shoppingActive(true).currency(Currency.TRY).build();
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder().amount(BigDecimal.TEN).destination("42234234234").destinationType(OppositePartyType.IBAN)
                .currency(Currency.TRY).build();
        when(walletRepository.findById(35L)).thenReturn(Optional.ofNullable(wallet));
        when(config.getMaxAmountToApprove()).thenReturn(BigDecimal.valueOf(1000));
        Assertions.assertThrows(InsufficientBalance.class, () -> {
            walletService.makeWithdraw(wallet.getCustomerId(), wallet.get_id(), withdrawalRequest);
        });
    }


}