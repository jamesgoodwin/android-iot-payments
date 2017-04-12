package com.judopay.iot.app.order;

import java.util.List;

import rx.Completable;
import rx.Observable;

interface OrderRepository {

    Observable<List<Order>> getAll();

    Completable add(Order order);

    Completable update(Order order);
}