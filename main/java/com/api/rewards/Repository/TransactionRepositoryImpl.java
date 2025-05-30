package com.api.rewards.Repository;
import com.api.rewards.Model.Transaction;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Mock implementation of TransactionRepository.
 */
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Override
    public List<Transaction> getAllTransactions() {
        return List.of(
                new Transaction("C001", 120, LocalDate.now().minusMonths(1)),
                new Transaction("C001", 75, LocalDate.now().minusMonths(2)),
                new Transaction("C002", 200, LocalDate.now().minusMonths(1)),
                new Transaction("C002", 50, LocalDate.now().minusMonths(3)),
                new Transaction("C003", 130, LocalDate.now().minusMonths(2))
        );
    }
}
