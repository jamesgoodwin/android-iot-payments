package com.judopay.iot.app.order;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Observable;

class InMemoryOrderRepository implements OrderRepository {

    private final List<Order> currentOrders;

    InMemoryOrderRepository() {
        this.currentOrders = new ArrayList<>();
    }

    @Override
    public Observable<List<Order>> getAll() {
        return Observable.just(currentOrders);
    }

    @Override
    public Completable add(Order order) {
        currentOrders.add(order);
        return Completable.complete();
    }

    @Override
    public Completable update(Order order) {
        return Completable.complete();
    }
}
