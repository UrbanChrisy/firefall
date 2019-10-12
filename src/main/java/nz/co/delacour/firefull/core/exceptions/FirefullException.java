package nz.co.delacour.firefull.core.exceptions;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class FirefullException extends RuntimeException {

    public FirefullException() {
        super();
    }

    public FirefullException(String message) {
        super(message);
    }

    public FirefullException(String message, Throwable cause) {
        super(message, cause);
    }

    public FirefullException(Throwable cause) {
        super(cause);
    }

    protected FirefullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
