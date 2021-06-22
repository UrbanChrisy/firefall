package nz.co.delacour.firefall.core.exceptions;


public class FirefallException extends RuntimeException {

    public FirefallException() {
        super();
    }

    public FirefallException(String message) {
        super(message);
    }

    public FirefallException(String message, Throwable cause) {
        super(message, cause);
    }

    public FirefallException(Throwable cause) {
        super(cause);
    }

    protected FirefallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
