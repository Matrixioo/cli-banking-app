package com.bank;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Create an account\n2. Log into account\n0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> db.createAccount();
                case 2 -> loginLoop(scanner, db);
                case 0 -> {
                    System.out.println("Bye!");
                    running = false;
                }
                default -> System.out.println("Unknown command.");
            }
        }
        scanner.close();
    }

    private static void loginLoop(Scanner scanner, DatabaseManager db) {
        System.out.print("Enter your card number: ");
        String cardInput = scanner.next();
        System.out.print("Enter your PIN: ");
        String pinInput = scanner.next();

        Account account = db.getAccount(cardInput);

        if (account == null || !account.checkPin(pinInput)) {
            System.out.println("Wrong card number or PIN!");
            return;
        }

        System.out.println("You have successfully logged in!");
        boolean loggedIn = true;

        while (loggedIn) {
            account = db.getAccount(cardInput);

            System.out.println("\n1. Balance\n2. Add income\n3. Do transfer\n4. Log out\n0. Exit");
            System.out.print("Choose an option: ");
            int action = scanner.nextInt();

            switch (action) {
                case 1 -> System.out.println("Balance: $" + account.balance());
                case 2 -> {
                    System.out.print("Enter income amount: ");
                    double income = scanner.nextDouble();
                    db.addIncome(account.cardNumber(), income);
                }
                case 3 -> {
                    System.out.print("Enter destination card number: ");
                    String destCard = scanner.next();
                    System.out.print("Enter amount to transfer: ");
                    double amount = scanner.nextDouble();

                    if (destCard.equals(account.cardNumber())) {
                        System.out.println("You can't transfer money to the same account!");
                    } else if (amount > account.balance()) {
                        System.out.println("Not enough money!");
                    } else if (db.getAccount(destCard) == null) {
                        System.out.println("Such a card does not exist.");
                    } else {
                        if (db.transferMoney(account.cardNumber(), destCard, amount)) {
                            System.out.println("Success!");
                        }
                    }
                }
                case 4 -> {
                    System.out.println("You have successfully logged out.");
                    loggedIn = false;
                }
                case 0 -> {
                    System.out.println("Bye!");
                    System.exit(0);
                }
                default -> System.out.println("Unknown command.");
            }
        }
    }
}