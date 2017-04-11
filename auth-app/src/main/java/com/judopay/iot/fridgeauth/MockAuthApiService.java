package com.judopay.iot.fridgeauth;

import java.util.ArrayList;
import java.util.List;

import rx.Single;

class MockAuthApiService implements AuthApiService {

    @Override
    public Single<List<Order>> getOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());

        return Single.just(orders);
    }

    @Override
    public Single<OrderResult> update(Order order, String orderId) {
        return Single.just(new OrderResult());
    }
}
