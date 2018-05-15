package com.cefalo.school;

public class Account {

    private String accountId;
    private String accountName;
    private int accountBalance;

    public String getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    Account(String accountId, String name) {
        this.accountId = accountId;
        this.accountName = name;
        this.accountBalance = 0;
    }

    Account(String accountId, String name, int amount) {
        this.accountId = accountId;
        this.accountName = name;
        this.accountBalance = amount;
    }    
    
    public void debitAccount(int amount) {
        this.accountBalance -= amount;
    }

    public void creditAccount(int amount) {
        this.accountBalance += amount;
    }
    
    @Override
    public String toString(){
        return "ID: "+this.accountId+"\t"+
                "Name: "+this.accountName+"\t"+
                "Balance: "+this.accountBalance;
                
    }

}
