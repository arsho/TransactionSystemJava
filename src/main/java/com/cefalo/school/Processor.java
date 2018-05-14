package com.cefalo.school;

public class Processor {

    public Transaction transaction;

    public boolean process(Transaction transaction) {
        this.transaction = transaction;
        if (this.transaction.transactionType == TransactionType.DEPOSIT_TYPE) {
            return this.processDeposit();
        } else if (this.transaction.transactionType == TransactionType.WITHDRAW_TYPE) {
            return this.processWithdraw();
        } else if (this.transaction.transactionType == TransactionType.TRANSFER_TYPE) {
            return this.processTransfer();
        }
        return false;
    }

    public boolean processDeposit() {
        this.transaction.sourceAccount.creditAccount(this.transaction.amount);
        this.transaction.isProcessed = true;
        return this.transaction.isProcessed;
    }

    public boolean processWithdraw() {
        if (this.transaction.sourceAccount.getAccountBalance() >= this.transaction.amount) {
            this.transaction.sourceAccount.debitAccount(this.transaction.amount);
            this.transaction.isProcessed = true;
        }
        return this.transaction.isProcessed;
    }

    public boolean processTransfer() {
        if (this.transaction.sourceAccount.getAccountBalance() >= this.transaction.amount) {
            this.transaction.sourceAccount.debitAccount(this.transaction.amount);
            this.transaction.destinationAccount.creditAccount(this.transaction.amount);
            this.transaction.isProcessed = true;
        }
        return this.transaction.isProcessed;
    }

}
