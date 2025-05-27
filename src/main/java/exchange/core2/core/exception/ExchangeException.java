package exchange.core2.core.exception;

import lombok.Getter;

@Getter
public class ExchangeException extends RuntimeException {
    private final String errorCode;

    public ExchangeException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ExchangeException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
} 