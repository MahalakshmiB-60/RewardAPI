package com.api.rewards.Service;

import com.api.rewards.Controller.RewardController;
import com.api.rewards.Model.RewardResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test for the RewardController class.
 * This test verifies the behavior of the controller when retrieving reward points for a customer.
 */
public class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    /**
     * Initializes mocks before each test.
     */
    public RewardControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for getRewardsByCustomer method.
     * Verifies that the controller returns the correct RewardResponse
     * when the service returns a mocked response for a given customer ID.
     */
    @Test
    public void testGetRewardsByCustomer() {
        // Arrange: Prepare mock data
        Map<String, Integer> monthlyPoints = Map.of("April", 120, "May", 100);
        RewardResponse mockResponse = new RewardResponse("C001", monthlyPoints, 220);

        // Mock the service call
        when(rewardService.calculateRewardsByCustomer("C001")).thenReturn(mockResponse);

        // Act: Call the controller method
        RewardResponse response = rewardController.getRewardsByCustomer("C001");

        // Assert: Validate the response
        assertEquals("C001", response.getCustomerId());
        assertEquals(220, response.getTotalPoints());
        assertEquals(2, response.getMonthlyPoints().size());
    }
}
