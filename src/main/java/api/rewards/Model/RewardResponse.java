package api.rewards.Model;

import java.util.Map;


/**
 * DTO to return reward points per customer.
 */
public class RewardResponse {
    private String customerId;
    private Map<String, Integer> monthlyPoints;
    private int totalPoints;
    /**
     * Constructor for RewardResponse.
     *
     * @param customerId the ID of the customer
     * @param monthlyPoints the reward points per month
     * @param totalPoints the total reward points
     */

    // Constructor, getters, and setters
    public RewardResponse(String customerId, Map<String, Integer> monthlyPoints, int totalPoints) {
        this.customerId = customerId;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Map<String, Integer> getMonthlyPoints() {
        return monthlyPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
