package hubs.digital.wallet.exception;

import org.springframework.http.HttpStatus;

public class UnprocessableWalletOperation extends BaseException {
    public UnprocessableWalletOperation(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
