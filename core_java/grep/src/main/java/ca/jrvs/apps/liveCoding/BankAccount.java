package ca.jrvs.apps.liveCoding;

public class BankAccount {

    private String accountNumber;
    private String ownerName;
    private double balance;

    public BankAccount(String accountNumber, String ownerName, double balance) {

        this.accountNumber = accountNumber;
        this.ownerName = ownerName;

        if (balance < 0) {
            this.balance = 0;
        } else {
            this.balance = balance;
        }
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {

        if (amount <= 0) {
            return;
        }

        balance += amount;
    }

    public void withdraw(double amount) {

        if (amount <= 0) {
            return;
        }

        if (amount > balance) {
            return;
        }

        balance -= amount;
    }

    public String getAccountInfo() {

        return "Account: " + accountNumber +
                "\nOwner: " + ownerName +
                "\nBalance: " + balance;
    }
}
