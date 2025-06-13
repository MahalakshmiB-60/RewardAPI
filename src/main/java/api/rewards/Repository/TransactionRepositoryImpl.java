package api.rewards.Repository;
import api.rewards.Model.Transaction;
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
                new Transaction("C003", 130, LocalDate.now().minusMonths(2)),
                new Transaction("C003", 90, LocalDate.now().minusDays(15)),
                new Transaction("C004", 110, LocalDate.now().minusDays(25)),
                new Transaction("C004", 70, LocalDate.now().minusDays(35)),
                new Transaction("C005", 95, LocalDate.now().minusDays(45)),
                new Transaction("C005", 105, LocalDate.now().minusDays(55)),
                new Transaction("C999", 150, LocalDate.now().minusMonths(6))

        );
    }
}

