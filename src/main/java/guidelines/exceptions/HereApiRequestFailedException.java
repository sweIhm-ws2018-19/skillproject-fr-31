package guidelines.exceptions;

public class HereApiRequestFailedException extends RuntimeException{
    public HereApiRequestFailedException() {
    }

    public HereApiRequestFailedException(String message) {
        super(message);
    }
}
