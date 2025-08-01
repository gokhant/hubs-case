package hubs.digital.wallet.common;

public enum TransactionStatus {
    PENDING, APPROVED, DENIED;

    public boolean isPending() {
        return this == PENDING;
    }
}