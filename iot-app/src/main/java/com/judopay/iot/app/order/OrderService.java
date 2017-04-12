package com.judopay.iot.app.order;

import android.content.Context;

import com.judopay.Judo;
import com.judopay.JudoApiService;
import com.judopay.model.Currency;
import com.judopay.model.TokenRequest;

import rx.Observable;
import rx.Subscription;

import static com.judopay.api.JudoApiServiceFactory.createApiService;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static rx.Observable.from;
import static rx.schedulers.Schedulers.io;

class OrderService {

    private final OrderRepository orderRepository;
    private final JudoApiService judoApiService;
    private final Judo judo;

    private Subscription subscription;

    OrderService(Context context, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

        judo = new Judo.Builder()
                .setJudoId("100407196")
                .setApiToken("ev7yQNcn8FPw6iFx")
                .setApiSecret("9c0e44fe99e5eb73d654b2c142ecf659b0d2a179356ddeca5ad2c5182eff56d4")
                .setEnvironment(Judo.SANDBOX)
                .build();

        this.judoApiService = createApiService(context, 0, judo);
    }

    void start() {
        subscription = orderRepository.getAll()
                .subscribeOn(io())
                .retryWhen(observable -> observable.delay(10, SECONDS))
                .flatMap(orders -> from(orders.stream()
                        .filter(Order::isReadyToPay)
                        .collect(toList())))
                .flatMap(order -> judoApiService.tokenPayment(new TokenRequest.Builder()
                        .setToken(order.getPaymentToken())
                        .setConsumerReference(order.getConsumerReference())
                        .setAmount(order.getTotal())
                        .setCurrency(Currency.GBP)
                        .setJudoId(judo.getJudoId())
                        .build())
                        .toObservable()
                        .flatMap(receipt -> Observable.just(new CompletedOrder(receipt, order))))
                .subscribe(completedOrder -> orderRepository.update(completedOrder.getOrder()));
    }

    void stop() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}