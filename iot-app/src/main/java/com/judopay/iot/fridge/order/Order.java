package com.judopay.iot.fridge.order;

import com.judopay.model.CardToken;

import java.util.UUID;

public class Order {

    private String total;
    private String description;

    public Order(String total, String description) {
        this.total = total;
        this.description = description;
    }

    String getDescription() {
        return description;
    }

    String getTotal() {
        return total;
    }

    boolean isReadyToPay() {
        return true;
    }

    public String getConsumerReference() {
        return "";
    }

    public CardToken getPaymentToken() {
        return new CardToken("", "", "", 0);
    }
}
