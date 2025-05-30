package com.api.rewards.Repository;
import com.api.rewards.Model.Transaction;
import java.util.List;



/**
 * Repository interface to fetch transactions.
 */
public interface TransactionRepository {
    List<Transaction> getAllTransactions();
}

