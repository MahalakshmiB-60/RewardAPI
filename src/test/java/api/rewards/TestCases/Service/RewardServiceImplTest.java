package api.rewards.TestCases.Service;

import api.rewards.Exception.CustomerNotFoundException;
import api.rewards.Exception.InvalidTransactionAmountException;
import api.rewards.Exception.TransactionNotFoundException;
import api.rewards.Model.RewardResponse;
import api.rewards.Model.Transaction;
import api.rewards.Repository.TransactionRepository;
import api.rewards.Service.RewardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RewardServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private List<Transaction> transactions;

    /**
     * Initializes mock objects and sets up sample transaction data before each test.
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
     * Test reward calculation for a valid customer with multiple recent transactions.
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
     *  Test that an exception is thrown when the customer ID is not found in any transaction.
     */
    @Test
    public void testCalculateRewardsByCustomer_NotFound() {
        when(transactionRepository.getAllTransactions()).thenReturn(transactions);
        Exception exception = assertThrows(CustomerNotFoundException.class, () ->
                rewardService.calculateRewardsByCustomer("C999"));
        assertEquals("Customer with ID C999 not found.", exception.getMessage());
    }

    /**
     *  Test that an exception is thrown when the transaction list is empty.
     */
    @Test
    public void testCalculateRewardsByCustomer_EmptyTransactionList() {
        when(transactionRepository.getAllTransactions()).thenReturn(List.of());
        Exception exception = assertThrows(CustomerNotFoundException.class, () ->
                rewardService.calculateRewardsByCustomer("C001"));
        assertEquals("Customer with ID C001 not found.", exception.getMessage());
    }

    /**
     *  Duplicate test to verify consistent behavior for a valid customer.
     */
    @Test
    public void testCalculateRewardsByCustomer_ValidCustomer() {
        when(transactionRepository.getAllTransactions()).thenReturn(transactions);
        RewardResponse response = rewardService.calculateRewardsByCustomer("C001");
        assertEquals("C001", response.getCustomerId());
        assertTrue(response.getTotalPoints() > 0);
    }

    /**
     *  Duplicate test to verify consistent exception handling for a non-existent customer.
     */
    @Test
    public void testCalculateRewardsByCustomer_CustomerNotFound() {
        when(transactionRepository.getAllTransactions()).thenReturn(transactions);
        assertThrows(CustomerNotFoundException.class, () ->
                rewardService.calculateRewardsByCustomer("C999"));
    }

    /**
     *  Test reward calculation for a customer with a high-value transaction.
     */
    @Test
    public void testCalculateRewardsByCustomer_HighValueTransaction() {
        List<Transaction> highValue = List.of(
                new Transaction("C001", 1000, LocalDate.now().minusDays(10))
        );
        when(transactionRepository.getAllTransactions()).thenReturn(highValue);
        RewardResponse response = rewardService.calculateRewardsByCustomer("C001");
        assertEquals(1850, response.getTotalPoints()); // 50 + (1000 - 100) * 2
    }

    /**
     *  Negative Test: Passing an empty string as customer ID should throw CustomerNotFoundException.
     */
    @Test
    public void testCalculateRewardsByCustomer_EmptyCustomerId() {
        when(transactionRepository.getAllTransactions()).thenReturn(transactions);
        assertThrows(CustomerNotFoundException.class, () ->
                rewardService.calculateRewardsByCustomer(""));
    }

    /**
     *  Negative Test: Transaction with negative amount should throw InvalidTransactionAmountException.
     */
    @Test
    public void testCalculateRewardsByCustomer_NegativeTransactionAmount() {
        List<Transaction> negativeAmountTx = List.of(
                new Transaction("C001", -50, LocalDate.now().minusDays(10))
        );
        when(transactionRepository.getAllTransactions()).thenReturn(negativeAmountTx);

        assertThrows(InvalidTransactionAmountException.class, () ->
                rewardService.calculateRewardsByCustomer("C001"));
    }

    /**
     *  Negative Test: Transactions older than three months
     */
    @Test
    void testCustomerExistsButNoRecentTransactions() {
        // Transaction older than 3 months
        Transaction oldTransaction = new Transaction("C123", 120.0, LocalDate.now().minusMonths(4));
        when(transactionRepository.getAllTransactions()).thenReturn(List.of(oldTransaction));

        assertThrows(TransactionNotFoundException.class, () -> {
            rewardService.calculateRewardsByCustomer("C123");
        });
    }



}
