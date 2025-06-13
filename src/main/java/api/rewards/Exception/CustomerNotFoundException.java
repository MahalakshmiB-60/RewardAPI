package api.rewards.Exception;


/**
 * Custom exception thrown when a customer id is not found.
 */
public class CustomerNotFoundException extends RuntimeException {
    /**
     * Constructor for CustomerNotFoundException.
     *
     * @param message the exception message
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
