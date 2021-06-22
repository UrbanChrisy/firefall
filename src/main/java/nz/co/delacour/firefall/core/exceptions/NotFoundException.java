package nz.co.delacour.firefall.core.exceptions;


public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        this("Entity not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
