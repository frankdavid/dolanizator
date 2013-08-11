package service;

public class GoobyPleaseException extends Exception {
    public GoobyPleaseException() {
    }

    public GoobyPleaseException(String message) {
        super(message);
    }

    public GoobyPleaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoobyPleaseException(Throwable cause) {
        super(cause);
    }

    public GoobyPleaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
