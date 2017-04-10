package com.judopay.iot.fridge.order;

import com.judopay.iot.fridge.device.DeviceRegisterRequest;
import com.judopay.iot.fridge.device.DeviceRegisterResult;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Single;

public interface IotApiService {

    @POST("/device")
    Single<DeviceRegisterResult> registerDevice(DeviceRegisterRequest request);

    @POST("/order")
    Single<OrderResult> order(@Body Order order);

    @GET("/order/{orderId}")
    Single<OrderResult> getStatus(@Path("orderId") String orderId);

}