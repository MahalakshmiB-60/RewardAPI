package api.rewards.TestCases.Controller;

import api.rewards.Exception.CustomerNotFoundException;
import api.rewards.Exception.TransactionNotFoundException;
import api.rewards.Model.RewardResponse;
import api.rewards.Controller.RewardController;
import api.rewards.Service.RewardService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the RewardController class.
 * These tests verify the behavior of the controller when retrieving reward points.
 */
public class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    public RewardControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     *  Test controller returns correct response for a valid customer with multiple months.
     */
    @Test
    public void testGetRewardsByCustomer() {
        Map<String, Integer> monthlyPoints = Map.of("April", 120, "May", 100);
        RewardResponse mockResponse = new RewardResponse("C001", monthlyPoints, 220);
        when(rewardService.calculateRewardsByCustomer("C001")).thenReturn(mockResponse);
        RewardResponse response = rewardController.getRewardsByCustomer("C001");
        assertEquals("C001", response.getCustomerId());
        assertEquals(220, response.getTotalPoints());
        assertEquals(2, response.getMonthlyPoints().size());
    }

    /**
     *  Test controller returns correct response for a valid customer with one month.
     */
    @Test
    public void testGetRewardsByCustomer_Success() {
        Map<String, Integer> points = Map.of("May", 100);
        RewardResponse mock = new RewardResponse("C001", points, 100);
        when(rewardService.calculateRewardsByCustomer("C001")).thenReturn(mock);
        RewardResponse response = rewardController.getRewardsByCustomer("C001");
        assertEquals("C001", response.getCustomerId());
    }

    /**
     *  Test controller throws exception when customer ID is not found.
     */
    @Test
    public void testGetRewardsByCustomer_NotFound() {
        when(rewardService.calculateRewardsByCustomer("C999"))
                .thenThrow(new CustomerNotFoundException("Customer with ID C999 not found."));
        assertThrows(CustomerNotFoundException.class, () ->
                rewardController.getRewardsByCustomer("C999"));
    }

    /**
     * Test controller throws exception when customer ID is null.
     */
    @Test
    public void testGetRewardsByCustomer_NullId() {
        when(rewardService.calculateRewardsByCustomer(null))
                .thenThrow(new NullPointerException("Customer ID cannot be null."));
        assertThrows(NullPointerException.class, () ->
                rewardController.getRewardsByCustomer(null));
    }

    /**
     *  Test controller returns list of rewards for all customers.
     */
    @Test
    public void testGetAllCustomerRewards_Success() {
        List<RewardResponse> mockList = List.of(
                new RewardResponse("C001", Map.of("May", 100), 100),
                new RewardResponse("C002", Map.of("May", 150), 150)
        );
        when(rewardService.calculateRewardsForAllCustomers()).thenReturn(mockList);
        List<RewardResponse> responses = rewardController.getAllCustomerRewards();
        assertEquals(2, responses.size());
    }

    /**
     *  Test controller returns empty list when no customer rewards are available.
     */
    @Test
    public void testGetAllCustomerRewards_EmptyList() {
        when(rewardService.calculateRewardsForAllCustomers()).thenReturn(List.of());
        List<RewardResponse> responses = rewardController.getAllCustomerRewards();
        assertTrue(responses.isEmpty());
    }

    /**
     * Test controller handles customer with zero reward points.
     */
    @Test
    public void testGetRewardsByCustomer_ZeroPoints() {
        Map<String, Integer> points = Map.of("May", 0);
        RewardResponse mock = new RewardResponse("C001", points, 0);
        when(rewardService.calculateRewardsByCustomer("C001")).thenReturn(mock);
        RewardResponse response = rewardController.getRewardsByCustomer("C001");
        assertEquals(0, response.getTotalPoints());
    }

    /**
     *  Test controller throws TransactionNotFoundException when customer has only old transactions.
     */
    @Test
    public void testGetRewardsByCustomer_NoRecentTransactions() {
        String customerId = "C123";
        when(rewardService.calculateRewardsByCustomer(customerId))
                .thenThrow(new TransactionNotFoundException("No transactions found in the last 3 months for customer ID " + customerId));

        assertThrows(TransactionNotFoundException.class, () -> {
            rewardController.getRewardsByCustomer(customerId);
        });
    }
}
