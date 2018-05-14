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
            if (transaction.isProcessed == false) {
                return true;
            }
        }
        return false;
    }

    public void processPendingTransactions() {
        for (Transaction transaction : this.transactionList) {
            if (transaction.isProcessed == false) {
                transaction.process();
            }
        }
    }

    public void rollbackTransaction(UUID transactionId) {
        // To do
    }
}
