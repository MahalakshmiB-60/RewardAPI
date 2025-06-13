package api.rewards.Exception;

/**
 * Custom exception thrown when a transaction has an invalid (e.g., negative) amount.
 */
public class InvalidTransactionAmountException extends RuntimeException {
    /**
     * Constructor for InvalidTransactionAmountException.
     *
     * @param message the exception message
     */
    public InvalidTransactionAmountException(String message) {
        super(message);
    }
}
