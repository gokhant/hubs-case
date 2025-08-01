package hubs.digital.wallet.common;

public enum TransactionType {
    DEPOSIT, WITHDRAWAL;

    public boolean isDeposit() {
        return DEPOSIT == this;
    }
}
