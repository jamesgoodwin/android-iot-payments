package com.judopay.iot.app;

import com.judopay.iot.app.device.Device;
import com.judopay.iot.app.order.Order;

import rx.Completable;

public interface ApiService {

    Completable addDeviceOrder(Order order, String deviceId);

    Completable addDevice(Device device);

}