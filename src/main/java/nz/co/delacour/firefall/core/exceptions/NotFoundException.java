package nz.co.delacour.firefall.core.exceptions;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("No entity was found");
    }

}
