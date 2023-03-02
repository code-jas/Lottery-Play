package com.angelo_silvestre.lotteryplay;

public class User {
    private String username = "Angelo";
    private int walletBalance = 1000;

    public User() {
    }

    public User(String username, int walletBalance) {
        this.username = username;
        this.walletBalance = walletBalance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        this.walletBalance = walletBalance;
    }
}
