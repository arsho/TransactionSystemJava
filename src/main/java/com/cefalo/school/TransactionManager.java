package com.cefalo.school;

import java.util.ArrayList;
import java.util.UUID;
import org.apache.commons.lang.NotImplementedException;

public class TransactionManager {

    // TODO: Maintain a List of Transactions (Deposit/Withdraw/ Transfer )
    // TODO: Add a Method for Adding Transaction
    // TODO: Add a method for Checking if there is any Pending Transtion
    ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

    public void addTransaction(Transaction transaction) {
        this.transactionList.add(transaction);
    }

    public boolean hasPendingTransactions() {
        for (Transaction transaction : this.transactionList) {
            if (transaction.isProcessed == false
                    || (transaction.isRollbackTried == true && transaction.isRollbacked == false)) {
                return true;
            }
        }
        return false;
    }

    public void processPendingTransactions() throws IllegalAccessException {
        for (Transaction transaction : this.transactionList) {
            if (transaction.isProcessed == false) {
                transaction.process();
            } else if (transaction.isRollbackTried == true && transaction.isRollbacked == false) {
                transaction.rollback();
            }
        }
    }

    public boolean rollbackTransaction(Transaction transaction) throws IllegalAccessException {
        return transaction.rollback();
    }
}
