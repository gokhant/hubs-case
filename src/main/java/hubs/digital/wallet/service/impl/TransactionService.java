package hubs.digital.wallet.service.impl;

import hubs.digital.wallet.api.request.WithdrawalRequest;
import hubs.digital.wallet.common.OppositePartyType;
import hubs.digital.wallet.common.TransactionStatus;
import hubs.digital.wallet.common.TransactionType;
import hubs.digital.wallet.api.request.DepositRequest;
import hubs.digital.wallet.dto.TransactionDto;
import hubs.digital.wallet.exception.ResourceNotFound;
import hubs.digital.wallet.model.Transaction;
import hubs.digital.wallet.model.Wallet;
import hubs.digital.wallet.repository.TransactionRepository;
import hubs.digital.wallet.repository.WalletRepository;
import hubs.digital.wallet.service.ITransactionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long createTransactionForDeposit(DepositRequest depositRequest, TransactionStatus status, Long walletId) {
        return createTransaction(walletId, TransactionType.DEPOSIT, depositRequest.getAmount()
                , depositRequest.getSourceType(), depositRequest.getSource(), status);
    }

    @Override
    public Long createTransactionForWithdraw(WithdrawalRequest withdrawalRequest, TransactionStatus status, Long walletId) {
        return createTransaction(walletId, TransactionType.WITHDRAWAL, withdrawalRequest.getAmount()
                , withdrawalRequest.getDestinationType(), withdrawalRequest.getDestination(), status);
    }

    @Override
    public List<TransactionDto> listTransactions(@Valid Long customerId, @Valid Long walletId) {
        return transactionRepository.findByWalletId(walletId).stream().map(transaction -> modelMapper.map(transaction, TransactionDto.class)).toList();
    }

    @Override
    @Transactional
    public TransactionDto updateStatus(Long walletId, Long transactionId, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow();
        if (transaction.getStatus().isPending()) {
            Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new ResourceNotFound(String.format("wallet %d not found", walletId)));
            if (transaction.getType().isDeposit()) {
                wallet.increaseUsableBalance(transaction.getAmount());
            } else {
                wallet.increaseBalance(transaction.getAmount().multiply(BigDecimal.valueOf(-1L)));
            }
            walletRepository.save(wallet);
            transaction.setStatus(status);
            return modelMapper.map(transactionRepository.save(transaction), TransactionDto.class);
        } else {
            // silently ignore
            return modelMapper.map(transaction, TransactionDto.class);
        }
    }

    private Long createTransaction(Long walletId, TransactionType type,  BigDecimal amount, OppositePartyType oppositePartyType, String oppositeParty, TransactionStatus status) {
        Transaction saved = transactionRepository.save(
                Transaction.builder().type(type)
                        .oppositePartType(oppositePartyType).oppositeParty(oppositeParty)
                        .amount(amount)
                        .walletId(walletId)
                        .status(status)
                        .build()
        );
        return saved.get_id();
    }
}