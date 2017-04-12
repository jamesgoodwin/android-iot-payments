package com.judopay.iot.app.order;

import com.judopay.model.Receipt;

class CompletedOrder {

    private final Receipt receipt;
    private final Order order;

    CompletedOrder(Receipt receipt, Order order) {
        this.receipt = receipt;
        this.order = order;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public Order getOrder() {
        return order;
    }
}
