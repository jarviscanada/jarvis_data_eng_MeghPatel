package ca.jrvs.apps.liveCoding;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {

            System.out.println("\nChoose an option:");
            System.out.println("1 - Fraud Detection");
            System.out.println("2 - Bank Account");
            System.out.println("0 - Exit");

            choice = scanner.nextInt();

            switch (choice) {

                // -------- FRAUD DETECTION --------
                case 1:

                    int n;

                    while (true) {
                        System.out.println("Enter number of transactions (positive number):");
                        n = scanner.nextInt();

                        if (n > 0) {
                            break;
                        }

                        System.out.println("Please enter a positive value.");
                    }

                    List<Integer> transactions = new ArrayList<>();

                    System.out.println("Enter the transactions:");

                    for (int i = 0; i < n; i++) {

                        int amount;

                        while (true) {
                            amount = scanner.nextInt();

                            if (amount >= 0) {
                                break;
                            }

                            System.out.println("Transaction must be positive. Enter again:");
                        }

                        transactions.add(amount);
                    }

                    int threshold;

                    while (true) {
                        System.out.println("Enter fraud threshold (positive number):");
                        threshold = scanner.nextInt();

                        if (threshold >= 0) {
                            break;
                        }

                        System.out.println("Please enter a positive value.");
                    }

                    List<Integer> fraud =
                            FraudDetection.detectFraud(transactions, threshold);

                    System.out.println("Fraud Transactions (Loop): " + fraud);

                    List<Integer> fraudStream =
                            FraudDetection.detectFraudStream(transactions, threshold);

                    System.out.println("Fraud Transactions (Stream): " + fraudStream);

                    break;



                // -------- BANK ACCOUNT --------
                case 2:

                    scanner.nextLine(); // clear buffer

                    System.out.println("Enter account number:");
                    String accountNumber = scanner.nextLine();

                    System.out.println("Enter owner name:");
                    String ownerName = scanner.nextLine();

                    double balance;

                    while (true) {
                        System.out.println("Enter starting balance:");
                        balance = scanner.nextDouble();

                        if (balance >= 0) {
                            break;
                        }

                        System.out.println("Balance must be positive. Enter again.");
                    }

                    BankAccount account =
                            new BankAccount(accountNumber, ownerName, balance);

                    double depositAmount;

                    while (true) {
                        System.out.println("Enter deposit amount:");
                        depositAmount = scanner.nextDouble();

                        if (depositAmount > 0) {
                            break;
                        }

                        System.out.println("Deposit must be positive. Enter again.");
                    }

                    account.deposit(depositAmount);

                    double withdrawAmount;

                    while (true) {
                        System.out.println("Enter withdrawal amount:");
                        withdrawAmount = scanner.nextDouble();

                        if (withdrawAmount > 0) {
                            break;
                        }

                        System.out.println("Withdrawal must be positive. Enter again.");
                    }

                    account.withdraw(withdrawAmount);

                    System.out.println("\nAccount Information:");
                    System.out.println(account.getAccountInfo());

                    break;



                // -------- EXIT --------
                case 0:
                    System.out.println("Exiting program...");
                    break;



                // -------- INVALID OPTION --------
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}
