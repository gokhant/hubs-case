package hubs.digital.wallet.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFound extends BaseException {
    public ResourceNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}