package hubs.digital.wallet.service;

import hubs.digital.wallet.api.request.WithdrawalRequest;
import hubs.digital.wallet.common.TransactionStatus;
import hubs.digital.wallet.api.request.DepositRequest;
import hubs.digital.wallet.dto.TransactionDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ITransactionService {
    Long createTransactionForDeposit(DepositRequest depositRequest, TransactionStatus status, Long walletId);

    Long createTransactionForWithdraw(WithdrawalRequest withdrawalRequest, TransactionStatus status, Long walletId);

    List<TransactionDto> listTransactions(@Valid Long customerId, @Valid Long walletId);

    TransactionDto updateStatus(@Valid Long walletId, @Valid Long transactionId, @Valid TransactionStatus status);
}
