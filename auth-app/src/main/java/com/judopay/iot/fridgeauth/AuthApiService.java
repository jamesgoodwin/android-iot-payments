package com.judopay.iot.fridgeauth;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Single;

public interface AuthApiService {

    @GET("/order")
    Single<List<Order>> getOrders();

    @POST("/order/{orderId}")
    Single<OrderResult> update(@Body Order order, @Path("orderId") String orderId);

}