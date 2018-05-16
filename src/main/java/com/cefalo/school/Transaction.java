package com.cefalo.school;

import java.util.UUID;

public class Transaction {

    public UUID transactionId;
    public TransactionType transactionType;
    public Account sourceAccount;
    public Account destinationAccount;
    public int amount;
    public boolean isProcessed;
    public boolean isRollbacked;
    public boolean isRollbackTried;

    Transaction(TransactionType transactionType, Account account, int amount) {
        this.transactionId = UUID.randomUUID();
        this.isProcessed = false;
        this.isRollbacked = false;
        this.isRollbackTried = false;
        this.transactionType = transactionType;
        this.sourceAccount = account;
        this.amount = amount;
    }

    Transaction(TransactionType transactionType, Account sourceAccount,
            Account destinationAccount,
            int amount) {
        this(transactionType, sourceAccount, amount);
        this.destinationAccount = destinationAccount;
    }

    public boolean process() {
        Processor processor = new Processor();
        return processor.process(this);
    }
    
    public boolean rollback() throws IllegalAccessException{
        Processor processor = new Processor();
        return processor.processRollback(this);
    }

    public boolean isPending() {
        return this.isProcessed;
    }

    public boolean getIsRollbacked() {
        return this.isRollbacked;
    }

    public boolean getIsRollbackedTried() {
        return this.isRollbackTried;
    }
}
