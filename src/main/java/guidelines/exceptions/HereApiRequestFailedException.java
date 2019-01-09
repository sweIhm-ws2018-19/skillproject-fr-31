package guidelines.exceptions;

public class HereApiRequestFailedException extends RuntimeException{

    public HereApiRequestFailedException(String message) {
        super(message);
    }
}
