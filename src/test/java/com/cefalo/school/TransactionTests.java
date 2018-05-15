package com.cefalo.school;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TransactionTests {

    @Test
    public void depositAccountCheckBalanceAndThenWithdraw_AllTransactionsSuccessful() {
        TransactionManager transactionManager = new TransactionManager();
        // Create Account with Balance 0
        Account nicola = new Account("123", "Nicola");

        // Add a deposit reqest of 100 to that account
        Transaction nicolaDeposit = new Transaction(TransactionType.DEPOSIT_TYPE, nicola, 100);

        // Pre-check : transactionManager Should have pending transactions at this point
        transactionManager.addTransaction(nicolaDeposit);
        boolean hasPendingTransactionsFlag = transactionManager.hasPendingTransactions();
        assertEquals(true, hasPendingTransactionsFlag);

        // Account Balance Should be 0 at this point
        int balance = nicola.getAccountBalance();
        assertEquals(0, balance);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Check : there should not be any pending Transactions now
        hasPendingTransactionsFlag = transactionManager.hasPendingTransactions();
        assertEquals(false, hasPendingTransactionsFlag);

        // Check balance of the account which should be 100 now
        balance = nicola.getAccountBalance();
        assertEquals(100, balance);

        // Create a Withdraw of 50 on that account
        Transaction nicolaWithdraw = new Transaction(TransactionType.WITHDRAW_TYPE, nicola, 50);
        transactionManager.addTransaction(nicolaWithdraw);

        // Perform a ProcessPendingTransactions() to process this
        transactionManager.processPendingTransactions();

        // check : No pending Transactions at this point
        hasPendingTransactionsFlag = transactionManager.hasPendingTransactions();
        assertEquals(false, hasPendingTransactionsFlag);

        // Check balance: should be 100-50 =50
        balance = nicola.getAccountBalance();
        assertEquals(50, balance);
    }

    @Test
    public void test_WithDrawRequestForAmountGreaterThanAvailableBalance_TransactionExecutedWhenBalanceConstrainMet() {

        TransactionManager transactionManager = new TransactionManager();
        // Create account with 75 as initial Balance
        Account pikachu = new Account("124", "Pikachu", 75);

        // Add a withdraw request of 100 (exceeding the available balance)
        Transaction pikachuWithdraw = new Transaction(TransactionType.WITHDRAW_TYPE, pikachu, 100);
        transactionManager.addTransaction(pikachuWithdraw);

        // Check Balance: should be 75
        int balance = pikachu.getAccountBalance();
        assertEquals(75, balance);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Add a deposit request to that account of 75
        Transaction pikachuDeposit = new Transaction(TransactionType.DEPOSIT_TYPE, pikachu, 75);
        transactionManager.addTransaction(pikachuDeposit);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // check : there should be pending transactions at this point
        boolean hasPendingTransactionsFlag = transactionManager.hasPendingTransactions();
        assertEquals(true, hasPendingTransactionsFlag);

        // check Balance: should be 150
        balance = pikachu.getAccountBalance();
        assertEquals(150, balance);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Check: there should be no pending transactions at this point
        hasPendingTransactionsFlag = transactionManager.hasPendingTransactions();
        assertEquals(false, hasPendingTransactionsFlag);

        // Balance Check : should be 50 (withdraw request of 100 should be successfull
        // at this point)
        balance = pikachu.getAccountBalance();
        assertEquals(50, balance);
    }

    @Test
    public void testTransferRequestForAmountGreaterThanAvailableBalanceTransactionExecutedWhenBalanceConstrainMet() {
        TransactionManager transactionManager = new TransactionManager();
        int balance;
        boolean hasPendingTransactionsFlag;

        // Create firstAccount with Initial Balance 100
        Account gilfoyle = new Account("125", "Gilfoyle", 100);

        // Create secondAccount with Initial Balance 2000
        Account dinesh = new Account("126", "Dinesh", 2000);

        // Create a transfer request of 700 from firstAccount to secondAccount
        Transaction gilfoyleToDineshTransfer = new Transaction(TransactionType.TRANSFER_TYPE, gilfoyle, dinesh, 700);
        transactionManager.addTransaction(gilfoyleToDineshTransfer);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Balance Check : FirstAccount -> 100
        balance = gilfoyle.getAccountBalance();
        assertEquals(100, balance);

        // Balance Check : SecondAccount -> 2000
        balance = dinesh.getAccountBalance();
        assertEquals(2000, balance);

        // Add a Deposit request of 900 to FirstAccount
        Transaction gilfoyleDeposit = new Transaction(TransactionType.DEPOSIT_TYPE, gilfoyle, 900);
        transactionManager.addTransaction(gilfoyleDeposit);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Balance Check : FirstAccount: 1000
        balance = gilfoyle.getAccountBalance();
        assertEquals(1000, balance);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Check: there should be no pending transactions at this point
        hasPendingTransactionsFlag = transactionManager.hasPendingTransactions();
        assertEquals(false, hasPendingTransactionsFlag);

        // Balance Check : FirstAccount -> 300
        balance = gilfoyle.getAccountBalance();
        assertEquals(300, balance);

        // Balance Check : SecondAccount -> 2700
        balance = dinesh.getAccountBalance();
        assertEquals(2700, balance);
    }

    @Test
    public void test_Transfer_ThenRollback_AccountStatusRegainedItsInitialState() {
        TransactionManager transactionManager = new TransactionManager();
        int balance;
        boolean hasPendingTransactionsFlag;

        // Create firstAccount with Initial Balance 2000
        Account leonard = new Account("127", "Leonard", 2000);

        // Create secondAccount with Initial Balance 100
        Account sheldon = new Account("128", "Sheldon", 100);

        // Create a transfer request of 700 from firstAccount to secondAccount
        Transaction leonardToSheldonTransfer = new Transaction(
                TransactionType.TRANSFER_TYPE, leonard, sheldon, 700);
        transactionManager.addTransaction(leonardToSheldonTransfer);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Balance Check : FirstAccount -> 1300
        balance = leonard.getAccountBalance();
        assertEquals(1300, balance);        

        // Balance Check : SecondAccount -> 800
        balance = sheldon.getAccountBalance();
        assertEquals(800, balance);        

        // Perform a Rollback with the transaction Id of the transfer which is made
        transactionManager.rollbackTransaction(leonardToSheldonTransfer);

        // Check: there should be no pending transactions at this point
        hasPendingTransactionsFlag = transactionManager.hasPendingTransactions();
        assertEquals(false, hasPendingTransactionsFlag);

        // Balance Check : FirstAccount -> 2000
        balance = leonard.getAccountBalance();
        assertEquals(2000, balance);        

        // Balance Check : SecondAccount -> 100
        balance = sheldon.getAccountBalance();
        assertEquals(100, balance);        
    }

    @Test
    public void test_Transfer_ThenWithdrawFromTheSecondAccount_ThenRollback() {
        TransactionManager transactionManager = new TransactionManager();
        int balance;
        boolean hasPendingTransactionsFlag;
        
        // Create firstAccount with Initial Balance 2000
        Account leonard = new Account("127", "Leonard", 2000);

        // Create secondAccount with Initial Balance 100
        Account sheldon = new Account("128", "Sheldon", 100);
        
        // Create a transfer request of 700 from firstAccount to secondAccount
        Transaction leonardToSheldonTransfer = new Transaction(
                TransactionType.TRANSFER_TYPE, leonard, sheldon, 700);
        transactionManager.addTransaction(leonardToSheldonTransfer);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Balance Check : FirstAccount -> 1300
        balance = leonard.getAccountBalance();
        assertEquals(1300, balance);        

        // Balance Check : SecondAccount -> 800
        balance = sheldon.getAccountBalance();
        assertEquals(800, balance);        

        // Create a withdraw request of 600 from the secondAccount
        Transaction sheldonWithdraw = new Transaction(
                TransactionType.WITHDRAW_TYPE, sheldon, 600);
        transactionManager.addTransaction(sheldonWithdraw);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Perform a Rollback with the transaction Id of the transfer which is made
        // (Rollback should not be executed because of balance constraint)
        transactionManager.rollbackTransaction(leonardToSheldonTransfer);

        // Perform a Rollback with the transaction Id of the withdraw which was made
        transactionManager.rollbackTransaction(sheldonWithdraw);

        // Run ProcessPendingTransactions() to process Pending TransactionRequests
        transactionManager.processPendingTransactions();

        // Balance Check : FirstAccount -> 2000
        balance = leonard.getAccountBalance();
        assertEquals(2000, balance);        

        // Balance Check : SecondAccount -> 100
        balance = sheldon.getAccountBalance();
        assertEquals(100, balance);        
    }
}
