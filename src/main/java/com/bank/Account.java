package com.bank;

public record Account(int id, String cardNumber, String pin, double balance) {

    public boolean checkPin(String inputPin) {
        return this.pin.equals(inputPin);
    }
}