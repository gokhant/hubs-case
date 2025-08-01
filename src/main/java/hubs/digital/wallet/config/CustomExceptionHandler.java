package hubs.digital.wallet.config;

import hubs.digital.wallet.api.response.ErrorResponse;
import hubs.digital.wallet.exception.BaseException;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "system is having technical difficulties");
    }

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(BaseException be) {
        return generateErrorResponse(be.getHttpStatus(), be.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException manve) {
        List<String> errors = manve.getBindingResult().getAllErrors().stream()
                .map(err -> err.unwrap(ConstraintViolation.class))
                .map(err -> String.format("'%s' %s", err.getPropertyPath(), err.getMessage())).toList();
        return generateErrorResponse(HttpStatus.BAD_REQUEST, errors.toString());
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(HttpStatus status, String errorMessage) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder().message(errorMessage).status(status.value()).build()
        );
    }
}