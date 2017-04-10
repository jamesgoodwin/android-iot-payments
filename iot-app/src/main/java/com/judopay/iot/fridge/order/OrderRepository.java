package com.judopay.iot.fridge.order;

import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Single;

interface OrderRepository {

    Observable<List<Order>> getAll();

    Completable add(Order order);

    Completable update(Order order);
}