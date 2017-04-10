package com.judopay.iot.fridge.order;

import com.judopay.iot.fridge.device.DeviceRegisterRequest;
import com.judopay.iot.fridge.device.DeviceRegisterResult;

import rx.Single;

class MockIotApiService implements IotApiService {

    @Override
    public Single<DeviceRegisterResult> registerDevice(DeviceRegisterRequest request) {
        return Single.just(new DeviceRegisterResult());
    }

    @Override
    public Single<OrderResult> order(Order order) {
        return Single.just(new OrderResult());
    }

    @Override
    public Single<OrderResult> getStatus(String orderId) {
        return Single.just(new OrderResult());
    }
}
