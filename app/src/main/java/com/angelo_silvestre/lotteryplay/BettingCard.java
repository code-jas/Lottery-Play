package com.angelo_silvestre.lotteryplay;

import java.util.ArrayList;

public class BettingCard {
    private int betPrice;
    private ArrayList<String> card = new ArrayList<String>();
    private int multiplier;



    public BettingCard(ArrayList<String> card) {
        this.card = card;
    }

    public BettingCard(int betPrice, ArrayList<String> card) {
        this.betPrice = betPrice;
        this.card = card;
    }

    public BettingCard(int betPrice, ArrayList<String> card, int multiplier) {
        this.betPrice = betPrice;
        this.card = card;
        this.multiplier = multiplier;
    }

    public int getBetPrice() {
        return betPrice;
    }

    public void setBetPrice(int betPrice) {
        this.betPrice = betPrice;
    }

    public ArrayList<String> getCard() {
        return card;
    }

    public void setCard(ArrayList<String> card) {
        this.card = card;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
