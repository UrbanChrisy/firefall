package nz.co.delacour.firefall.core.exceptions;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

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
