package com.api.rewards.Controller;
import com.api.rewards.Model.RewardResponse;
import org.springframework.web.bind.annotation.*;
import  com.api.rewards.Service.RewardService;

/**
 * REST controller to expose reward calculation endpoints.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * Get reward points for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return reward points for the customer
     */
    @GetMapping("/{customerId}")
    public RewardResponse getRewardsByCustomer(@PathVariable String customerId) {
        return rewardService.calculateRewardsByCustomer(customerId);
    }
}
