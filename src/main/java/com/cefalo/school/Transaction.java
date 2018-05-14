package com.cefalo.school;

import java.util.UUID;

public class Transaction {

    public UUID transactionId;
    public TransactionType transactionType;
    public Account sourceAccount;
    public Account destinationAccount;
    public int amount;
    public boolean isProcessed;

    Transaction(TransactionType transactionType, Account account, int amount) {
        this.transactionId = UUID.randomUUID();
        this.isProcessed = false;
        this.transactionType = transactionType;
        this.sourceAccount = account;
        this.amount = amount;
    }

    Transaction(TransactionType transactionType, Account sourceAccount, 
            Account destinationAccount,
            int amount) {
        this.transactionId = UUID.randomUUID();
        this.isProcessed = false;
        this.transactionType = transactionType;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;        
    }
    /* Process transaction */
    public boolean process() {
        Processor processor = new Processor();
        this.isProcessed = processor.process(this);
        return this.isProcessed;
    }

    public boolean isPending() {
        return this.isProcessed;
    }
}
