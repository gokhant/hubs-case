package hubs.digital.wallet.common;

public enum OppositePartyType {
    IBAN, PAYMENT;

    public boolean isIBAN() {
        return this == IBAN;
    }

    public boolean isPAYMENT() {
        return this == PAYMENT;
    }
}