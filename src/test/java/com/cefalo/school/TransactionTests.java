package com.cefalo.school;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TransactionTests {

    private TransactionManager transactionManager = new TransactionManager();

    @Test
    public void depositAccountCheckBalanceAndThenWithdraw_AllTransactionsSuccessful() {
        // Create Account with Balance 0
        Account nicola = new Account("123", "Nicola");

        // Add a deposit reqest of 100 to that account
        Transaction nicolaDeposit = new Transaction(TransactionType.DEPOSIT_TYPE, nicola, 100);

        // Pre-check : transactionManager Should have pending transactions at this point
        this.transactionManager.addTransaction(nicolaDeposit);
        boolean hasPendingTransactionsFlag = this.transactionManager.hasPendingTransactions();
        System.out.println(hasPendingTransactionsFlag);
        assertEquals(true, hasPendingTransactionsFlag);

        // Account Balance Should be 0 at this point
        int balance = nicola.getAccountBalance();
        System.out.println(nicola.toString());
        assertEquals(0, balance);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        this.transactionManager.processPendingTransactions();

        // Check : there should not be any pending Transactions now
        hasPendingTransactionsFlag = this.transactionManager.hasPendingTransactions();
        System.out.println(hasPendingTransactionsFlag);
        assertEquals(false, hasPendingTransactionsFlag);

        // Check balance of the account which should be 100 now
        balance = nicola.getAccountBalance();
        System.out.println(nicola.toString());
        assertEquals(100, balance);

        // Create a Withdraw of 50 on that account
        Transaction nicolaWithdraw = new Transaction(TransactionType.WITHDRAW_TYPE, nicola, 50);
        this.transactionManager.addTransaction(nicolaWithdraw);

        // Perform a ProcessPendingTransactions() to process this
        this.transactionManager.processPendingTransactions();

        // check : No pending Transactions at this point
        hasPendingTransactionsFlag = this.transactionManager.hasPendingTransactions();
        System.out.println(hasPendingTransactionsFlag);
        assertEquals(false, hasPendingTransactionsFlag);

        // Check balance: should be 100-50 =50
        balance = nicola.getAccountBalance();
        System.out.println(nicola.toString());
        assertEquals(50, balance);

    }

    @Test
    public void test_WithDrawRequestForAmountGreaterThanAvailableBalance_TransactionExecutedWhenBalanceConstrainMet() {
        // Create account with 75 as initial Balance
        // Add a withdraw request of 100 (exceeding the available balance)
        // Check Balance: should be 75

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Add a deposit request to that account of 75
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // check : there should be pending transactions at this point
        // check Balance: should be 150
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Check: there should be no pending transactions at this point
        // Balance Check : should be 50 (withdraw request of 100 should be successfull
        // at this point)
    }

    @Test
    public void testTransferRequestForAmountGreaterThanAvailableBalanceTransactionExecutedWhenBalanceConstrainMet() {
        // Create firstAccount with Initial Balance 100
        // Create secondAccount with Initial Balance 2000

        // Create a transfer request of 700 from firstAccount to secondAccount
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Balance Check : FirstAccount -> 100
        // Balance Check : SecondAccount -> 2000
        // Add a Deposit request of 900 to FirstAccount
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Balance Check : FirstAccount: 1000
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Check: there should be no pending transactions at this point
        // Balance Check : FirstAccount -> 300
        // Balance Check : SecondAccount -> 2700
    }

    @Test
    public void test_Transfer_ThenRollback_AccountStatusRegainedItsInitialState() {
        // Create firstAccount with Initial Balance 2000
        // Create secondAccount with Initial Balance 100

        // Create a transfer request of 700 from firstAccount to secondAccount
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Balance Check : FirstAccount -> 1300
        // Balance Check : SecondAccount -> 800
        // Perform a Rollback with the transaction Id of the transfer which is made
        // Check: there should be no pending transactions at this point
        // Balance Check : FirstAccount -> 2000
        // Balance Check : SecondAccount -> 100
    }

    @Test
    public void test_Transfer_ThenWithdrawFromTheSecondAccount_ThenRollback() {
        // Create firstAccount with Initial Balance 2000
        // Create secondAccount with Initial Balance 100

        // Create a transfer request of 700 from firstAccount to secondAccount
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Balance Check : FirstAccount -> 1300
        // Balance Check : SecondAccount -> 800
        // Create a withdraw request of 600 from the secondAccount
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Perform a Rollback with the transaction Id of the transfer which is made
        // (Rollback should not be executed because of balance constraint)
        // Perform a Rollback with the transaction Id of the withdraw which was made
        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        // Balance Check : FirstAccount -> 2000
        // Balance Check : SecondAccount -> 100
    }
}
