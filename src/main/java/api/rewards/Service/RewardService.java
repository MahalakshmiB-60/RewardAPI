package api.rewards.Service;


import api.rewards.Model.RewardResponse;

import java.util.List;

/**
 * Service interface for calculating rewards.
 */
public interface RewardService {
    /**
     * Calculate reward points for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return reward points for the customer
     */
    RewardResponse calculateRewardsByCustomer(String customerId);
    /**
     * Calculate reward points for all customer.
     *
     *
     * @return reward points for all customer
     */
    List<RewardResponse> calculateRewardsForAllCustomers();
}

