package api.rewards.Model;

import java.time.LocalDate;

/**
 * Represents a customer's transaction.
 */
public class Transaction {
    private String customerId;
    private double amount;
    private LocalDate date;
    // Constructor, getters, and setters
    /**
     * Constructor for Transaction.
     *
     * @param customerId the ID of the customer
     * @param date the date of the transaction
     * @param amount the amount of the transaction
     */
    public Transaction(String customerId, double amount, LocalDate date) {
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
