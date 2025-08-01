package hubs.digital.wallet.service.impl;

import hubs.digital.wallet.api.request.WithdrawalRequest;
import hubs.digital.wallet.api.response.WalletOperationResponse;
import hubs.digital.wallet.common.TransactionStatus;
import hubs.digital.wallet.config.WalletConfig;
import hubs.digital.wallet.api.request.DepositRequest;
import hubs.digital.wallet.api.request.WalletCreateRequest;
import hubs.digital.wallet.dto.WalletDto;
import hubs.digital.wallet.exception.InsufficientBalance;
import hubs.digital.wallet.exception.ResourceNotFound;
import hubs.digital.wallet.exception.UnprocessableWalletOperation;
import hubs.digital.wallet.model.Wallet;
import hubs.digital.wallet.repository.WalletRepository;
import hubs.digital.wallet.service.ITransactionService;
import hubs.digital.wallet.service.IWalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService implements IWalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final WalletConfig config;
    private final ITransactionService transactionService;

    @Override
    public Long createWallet(Long customerId, WalletCreateRequest walletCreateRequest) {
        Wallet wallet = Wallet.builder()
                .walletName(walletCreateRequest.getWalletName())
                .currency(walletCreateRequest.getCurrency())
                .customerId(customerId)
                .shoppingActive(walletCreateRequest.isShoppingActive())
                .withdrawActive(walletCreateRequest.isWithdrawActive())
                .build();
        Wallet created = walletRepository.save(wallet);
        return created.get_id();
    }

    @Override
    public List<WalletDto> listWallets(Long customerId) {
        return walletRepository.findByCustomerId(customerId)
                .stream().map(wallet -> modelMapper.map(wallet, WalletDto.class)).toList();
    }


    @Override
    @Transactional
    public WalletOperationResponse makeDeposit(Long customerId, Long walletId, DepositRequest depositRequest) {
        TransactionStatus status;
        BigDecimal changeForUsableBalance;
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new ResourceNotFound(String.format("wallet %d not found", walletId)));
        if (depositRequest.getCurrency() != wallet.getCurrency()) {
            log.warn("request {} and target {} currencies must not match for deposit", depositRequest.getCurrency(), wallet.getCurrency());
            throw new UnprocessableWalletOperation(String.format("request %s and target %s currencies must match for deposit", depositRequest.getCurrency(), wallet.getCurrency()));
        }
        if (depositRequest.getAmount().compareTo(config.getMaxAmountToApprove()) > 0) {
            status = TransactionStatus.PENDING;
            changeForUsableBalance = BigDecimal.ZERO;
        } else {
            status = TransactionStatus.APPROVED;
            changeForUsableBalance = depositRequest.getAmount();
        }
        wallet.increaseBalance(depositRequest.getAmount());
        wallet.increaseUsableBalance(changeForUsableBalance);
        Wallet saved = walletRepository.save(wallet);
        Long transactionId = transactionService.createTransactionForDeposit(depositRequest, status, walletId);
        return WalletOperationResponse.builder().balance(saved.getBalance())
                .usableBalance(saved.getUsableBalance())
                .transactionStatus(status)
                .transactionId(transactionId)
                .build();
    }

    @Override
    @Transactional
    public WalletOperationResponse makeWithdraw(Long customerId, Long walletId, WithdrawalRequest withdrawalRequest) {
        TransactionStatus status;
        BigDecimal changeForBalance;
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new ResourceNotFound(String.format("wallet %d not found", walletId)));
        if (withdrawalRequest.getDestinationType().isIBAN()) {
            if (!wallet.isWithdrawActive()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "withdrawals are not allowed for this wallet");
            }
        }
        if (withdrawalRequest.getDestinationType().isPAYMENT()) {
            if (!wallet.isWithdrawActive() || !wallet.isShoppingActive()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "payments are not allowed for this wallet");
            }
        }
        if (withdrawalRequest.getCurrency() != wallet.getCurrency()) {
            log.warn("request {} and target {} currencies do not match", withdrawalRequest.getCurrency(), wallet.getCurrency());
            throw new UnprocessableWalletOperation(String.format("request %s and target %s currencies must match for withdrawal", withdrawalRequest.getCurrency(), wallet.getCurrency()));
        }
        if (wallet.getUsableBalance().compareTo(withdrawalRequest.getAmount()) < 0) {
            log.warn("{} failed due to insufficient balance {} vs {} for wallet {} of {}", withdrawalRequest, wallet.getUsableBalance(), withdrawalRequest.getAmount(), walletId, customerId);
            throw new InsufficientBalance(String.format("%s failed, insufficient balance %.2f", withdrawalRequest, wallet.getUsableBalance()));
        }
        if (withdrawalRequest.getAmount().compareTo(config.getMaxAmountToApprove()) > 0) {
            status = TransactionStatus.PENDING;
            changeForBalance = BigDecimal.ZERO;
        } else {
            status = TransactionStatus.APPROVED;
            changeForBalance = withdrawalRequest.getAmount();
        }
        wallet.increaseUsableBalance(withdrawalRequest.getAmount().multiply(BigDecimal.valueOf(-1L)));
        wallet.increaseBalance(changeForBalance.multiply(BigDecimal.valueOf(-1L)));
        Wallet saved = walletRepository.save(wallet);
        Long transactionId = transactionService.createTransactionForWithdraw(withdrawalRequest, status, walletId);
        return WalletOperationResponse.builder().balance(saved.getBalance())
                .usableBalance(saved.getUsableBalance())
                .transactionStatus(status)
                .transactionId(transactionId)
                .build();
    }
}