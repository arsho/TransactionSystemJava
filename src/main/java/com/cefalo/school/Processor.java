package com.cefalo.school;

//import org.apache.commons.lang.UnhandledException;
public class Processor {

    public Transaction transaction;

    public boolean process(Transaction transaction) {
        this.transaction = transaction;
        boolean isSucceed = false;
        if (null != this.transaction.transactionType) {
            switch (this.transaction.transactionType) {
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
        }
        this.transaction.isProcessed = isSucceed;
        return isSucceed;
    }

    public boolean processRollback(Transaction transaction) {
        this.transaction = transaction;
        if (this.transaction.isRollbacked == true) {
            System.out.println(transaction.transactionId + " is already rollbacked");
        }
        this.transaction = transaction;
        boolean isRollbacked = false;
        if (null != this.transaction.transactionType) {
            switch (this.transaction.transactionType) {
                case DEPOSIT_TYPE:
                    isRollbacked = this.processWithdraw();
                    break;
                case WITHDRAW_TYPE:
                    isRollbacked = this.processDeposit();
                    break;
                case TRANSFER_TYPE:
                    isRollbacked = this.processTransferRollback();
                    break;
                default:
                    break;
            }
        }
        this.transaction.isRollbacked = isRollbacked;
        this.transaction.isRollbackTried = true;
        return isRollbacked;
    }

    public boolean processDeposit() {
        this.transaction.sourceAccount.creditAccount(this.transaction.amount);
        return true;
    }

    public boolean processWithdraw() {
        boolean isSuccess = false;
        if (this.transaction.sourceAccount.getAccountBalance() >= this.transaction.amount) {
            this.transaction.sourceAccount.debitAccount(this.transaction.amount);
            isSuccess = true;
        }
        return isSuccess;
    }

    public boolean processTransfer() {
        boolean isSuccess = false;
        if (this.transaction.sourceAccount.getAccountBalance() >= this.transaction.amount) {
            this.transaction.sourceAccount.debitAccount(this.transaction.amount);
            this.transaction.destinationAccount.creditAccount(this.transaction.amount);
            isSuccess = true;
        }
        return isSuccess;
    }

    public boolean processTransferRollback() {
        boolean isSuccess = false;
        if (this.transaction.destinationAccount.getAccountBalance() >= this.transaction.amount) {
            this.transaction.destinationAccount.debitAccount(this.transaction.amount);
            this.transaction.sourceAccount.creditAccount(this.transaction.amount);
            isSuccess = true;
        }
        return isSuccess;
    }

}
