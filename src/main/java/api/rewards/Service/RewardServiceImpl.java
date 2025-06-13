package api.rewards.Service;
import api.rewards.Exception.CustomerNotFoundException;
import api.rewards.Exception.InvalidTransactionAmountException;
import api.rewards.Exception.TransactionNotFoundException;
import api.rewards.Model.RewardResponse;
import api.rewards.Model.Transaction;
import api.rewards.Repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.format.TextStyle;
import java.util.*;


/**
 * Implementation of RewardService to calculate reward points.
 */
@Service
public class RewardServiceImpl implements RewardService {


    private final TransactionRepository repository;
    /**
     * Constructor for RewardServiceImpl.
     *
     * @param repository the repository to access transactions
     */
    public RewardServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }
    // Implementation To fetch a particular customer txn and calculating rewards

    @Override
    public RewardResponse calculateRewardsByCustomer(String customerId) {
        List<Transaction> transactions = repository.getAllTransactions();
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        Map<String, Integer> monthlyPoints = new HashMap<>();
        boolean customerExists = false;
        boolean hasRecentTransactions = false;

        for (Transaction tx : transactions) {
            if (!tx.getCustomerId().equals(customerId)) continue;
            customerExists = true;
            if (tx.getDate().isBefore(threeMonthsAgo)) continue;

            hasRecentTransactions = true;
            int points = calculatePoints(tx.getAmount());
            String month = tx.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthlyPoints.merge(month, points, Integer::sum);
        }

        if (!customerExists) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }

        if (!hasRecentTransactions) {
            throw new TransactionNotFoundException("No transactions found in the last 3 months for customer ID " + customerId);
        }

        int total = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();
        return new RewardResponse(customerId, monthlyPoints, total);
    }

    // Implementation To fetch all customers txn and calculating rewards
    @Override
    public List<RewardResponse> calculateRewardsForAllCustomers() {
        List<Transaction> transactions = repository.getAllTransactions();
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        Map<String, Map<String, Integer>> customerMonthlyPoints = new HashMap<>();

        for (Transaction tx : transactions) {
            if (tx.getDate().isBefore(threeMonthsAgo)) continue;

            int points = calculatePoints(tx.getAmount());
            String month = tx.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            customerMonthlyPoints
                    .computeIfAbsent(tx.getCustomerId(), k -> new HashMap<>())
                    .merge(month, points, Integer::sum);
        }

        List<RewardResponse> rewardResponses = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> entry : customerMonthlyPoints.entrySet()) {
            int total = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            rewardResponses.add(new RewardResponse(entry.getKey(), entry.getValue(), total));
        }

        return rewardResponses;
    }



    /**
     * Calculate reward points based on transaction amount.
     *
     * @param amount the transaction amount
     * @return reward points
     */
    private int calculatePoints(double amount) {
         int points = 0;
         if (amount < 0)
         {
         throw new InvalidTransactionAmountException("Transaction amount cannot be negative.");
         }
         else if (amount > 100)
         {
            points += (int) ((amount - 100) * 2);
            points += 50;
         }
         else if (amount > 50)
         {
            points += (int) (amount - 50);
         }
        return points;
    }
}
