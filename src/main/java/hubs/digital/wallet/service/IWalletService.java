package hubs.digital.wallet.service;

import hubs.digital.wallet.api.request.DepositRequest;
import hubs.digital.wallet.api.request.WalletCreateRequest;
import hubs.digital.wallet.api.request.WithdrawalRequest;
import hubs.digital.wallet.api.response.WalletOperationResponse;
import hubs.digital.wallet.dto.WalletDto;
import jakarta.validation.Valid;

import java.util.List;

public interface IWalletService {
    Long createWallet(Long customerId, WalletCreateRequest walletCreateRequest);

    List<WalletDto> listWallets(@Valid Long customerId);

    WalletOperationResponse makeDeposit(@Valid Long customerId, @Valid Long walletId, @Valid DepositRequest depositRequest);

    WalletOperationResponse makeWithdraw(@Valid Long customerId, @Valid Long walletId, @Valid WithdrawalRequest withdrawalRequest);
}