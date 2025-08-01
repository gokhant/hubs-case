package hubs.digital.wallet.exception;

import org.springframework.http.HttpStatus;

public class InsufficientBalance extends BaseException {
    public InsufficientBalance(String message) {
        super(message, HttpStatus.PAYMENT_REQUIRED);
    }
}
