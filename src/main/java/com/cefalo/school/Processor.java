package com.cefalo.school;

public class Processor {

    public Transaction transaction;

    public boolean process(Transaction transaction) {
        this.transaction = transaction;
        boolean isSucceed = false;
        if (null != this.transaction.transactionType) switch (this.transaction.transactionType) {
            case DEPOSIT_TYPE:
                isSucceed = this.processDeposit();
                break;
            case WITHDRAW_TYPE:
                isSucceed = this.processWithdraw();
                break;
            case TRANSFER_TYPE:
                isSucceed = this.processTransfer();
                break;
            default:
                break;
        }
        this.transaction.isProcessed = isSucceed;
        return isSucceed;
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
