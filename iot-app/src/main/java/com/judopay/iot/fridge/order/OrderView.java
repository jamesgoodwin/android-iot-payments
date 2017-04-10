package com.judopay.iot.fridge.order;

import com.judopay.iot.fridge.device.DeviceRegisterResult;

import java.util.List;

interface OrderView {

    void showOrders(List<Order> orders);

    void showDeviceId(String deviceId);

    void showDeviceRegistration(DeviceRegisterResult deviceRegisterResult);
}
