package com.api.rewards.Service;


import com.api.rewards.Model.RewardResponse;

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
}

