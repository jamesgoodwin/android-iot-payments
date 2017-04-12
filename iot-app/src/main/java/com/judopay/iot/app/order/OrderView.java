package com.judopay.iot.app.order;

import com.judopay.iot.app.device.DeviceRegisterResult;

import java.util.List;

interface OrderView {

    void showOrders(List<Order> orders);

    void showDeviceId(String deviceId);

    void showDeviceRegistration(DeviceRegisterResult deviceRegisterResult);
}
