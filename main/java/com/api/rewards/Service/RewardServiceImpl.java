package com.api.rewards.Service;
import com.api.rewards.Exception.CustomerNotFoundException;
import com.api.rewards.Model.RewardResponse;
import com.api.rewards.Model.Transaction;
import com.api.rewards.Repository.TransactionRepository;
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

    @Override
    public RewardResponse calculateRewardsByCustomer(String customerId) {
        List<Transaction> transactions = repository.getAllTransactions();
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        Map<String, Integer> monthlyPoints = new HashMap<>();
        boolean hasTransactions = false;

        for (Transaction tx : transactions) {
            if (!tx.getCustomerId().equals(customerId)) continue;
            hasTransactions = true;
            if (tx.getDate().isBefore(threeMonthsAgo)) continue;

            int points = calculatePoints(tx.getAmount());
            String month = tx.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthlyPoints.merge(month, points, Integer::sum);
        }

        if (!hasTransactions) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }

        int total = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();
        return new RewardResponse(customerId, monthlyPoints, total);
    }
    /**
     * Calculate reward points based on transaction amount.
     *
     * @param amount the transaction amount
     * @return reward points
     */
    private int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) ((amount - 100) * 2);
            points += 50;
        } else if (amount > 50) {
            points += (int) (amount - 50);
        }
        return points;
    }
}
