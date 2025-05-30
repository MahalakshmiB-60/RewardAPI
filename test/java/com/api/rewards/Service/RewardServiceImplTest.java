package com.api.rewards.Service;

import com.api.rewards.Exception.CustomerNotFoundException;
import com.api.rewards.Model.RewardResponse;
import com.api.rewards.Model.Transaction;
import com.api.rewards.Repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the RewardServiceImpl class.
 * These tests verify the reward calculation logic and exception handling.
 */
public class RewardServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private List<Transaction> transactions;

    /**
     * Initializes mocks and sets up sample transaction data before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactions = List.of(
                new Transaction("C001", 120, LocalDate.now().minusMonths(1)),
                new Transaction("C001", 75, LocalDate.now().minusMonths(2)),
                new Transaction("C002", 200, LocalDate.now().minusMonths(1))
        );
    }

    /**
     * Test case to verify reward calculation for a valid customer with transactions.
     * Ensures that the total points and monthly breakdown are correctly returned.
     */
    @Test
    public void testCalculateRewardsByCustomer_Success() {
        when(transactionRepository.getAllTransactions()).thenReturn(transactions);

        RewardResponse response = rewardService.calculateRewardsByCustomer("C001");

        assertEquals("C001", response.getCustomerId());
        assertTrue(response.getTotalPoints() > 0);
        assertFalse(response.getMonthlyPoints().isEmpty());
    }

    /**
     * Test case to verify that a CustomerNotFoundException is thrown
     * when the customer ID does not exist in the transaction list.
     */
    @Test
    public void testCalculateRewardsByCustomer_NotFound() {
        when(transactionRepository.getAllTransactions()).thenReturn(transactions);

        Exception exception = assertThrows(CustomerNotFoundException.class, () ->
                rewardService.calculateRewardsByCustomer("C999"));

        assertEquals("Customer with ID C999 not found.", exception.getMessage());
    }

    /**
     * Test case to verify that a CustomerNotFoundException is thrown
     * when the transaction list is empty.
     */
    @Test
    public void testCalculateRewardsByCustomer_EmptyTransactionList() {
        when(transactionRepository.getAllTransactions()).thenReturn(List.of());

        Exception exception = assertThrows(CustomerNotFoundException.class, () ->
                rewardService.calculateRewardsByCustomer("C001"));

        assertEquals("Customer with ID C001 not found.", exception.getMessage());
    }
}
