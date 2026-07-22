package com.bank;

import java.sql.*;
import java.util.Random;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:bank.db";

    public DatabaseManager() {
        initDatabase();
    }

    private void initDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                card_number TEXT UNIQUE NOT NULL,
                pin TEXT NOT NULL,
                balance REAL DEFAULT 0.0
            );
        """;
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    public Account createAccount() {
        String cardNumber = generateCardNumber();
        String pin = generatePin();
        String sql = "INSERT INTO accounts (card_number, pin) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
            System.out.println("Account created successfully!");
            System.out.println("Your card number: " + cardNumber);
            System.out.println("Your PIN: " + pin);
            return getAccount(cardNumber);
        } catch (SQLException e) {
            System.out.println("Error creating account.");
            return null;
        }
    }

    public Account getAccount(String cardNumber) {
        String sql = "SELECT * FROM accounts WHERE card_number = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getString("card_number"),
                        rs.getString("pin"),
                        rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            System.out.println("Database error.");
        }
        return null;
    }

    public void addIncome(String cardNumber, double amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE card_number = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, cardNumber);
            pstmt.executeUpdate();
            System.out.println("Income added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding income.");
        }
    }

    public boolean transferMoney(String fromCard, String toCard, double amount) {
        String withdrawSql = "UPDATE accounts SET balance = balance - ? WHERE card_number = ?";
        String depositSql = "UPDATE accounts SET balance = balance + ? WHERE card_number = ?";

        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement withdrawStmt = conn.prepareStatement(withdrawSql);
                 PreparedStatement depositStmt = conn.prepareStatement(depositSql)) {

                withdrawStmt.setDouble(1, amount);
                withdrawStmt.setString(2, fromCard);
                withdrawStmt.executeUpdate();

                depositStmt.setDouble(1, amount);
                depositStmt.setString(2, toCard);
                depositStmt.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction failed. Rolling back.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error during transfer.");
            return false;
        }
    }

    private String generateCardNumber() {
        Random random = new Random();
        long number = 4000000000000000L + (long) (random.nextDouble() * 1000000000000000L);
        return String.valueOf(number);
    }

    private String generatePin() {
        return String.format("%04d", new Random().nextInt(10000));
    }
}