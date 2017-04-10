package com.judopay.iot.fridge.order;

import android.content.Context;

import com.judopay.DeviceDna;
import com.judopay.devicedna.Credentials;
import com.judopay.iot.fridge.device.DeviceRegisterRequest;

import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class OrderPresenter {

    private final IotApiService iotApiService;
    private final OrderRepository orderRepository;
    private final OrderView orderView;

    OrderPresenter(OrderView orderView, OrderRepository orderRepository) {
        this.orderView = orderView;
        this.orderRepository = orderRepository;
        this.iotApiService = new MockIotApiService();
    }

    public void order(Order order) {
        orderRepository.add(order)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(() -> iotApiService.order(order)
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .subscribe(orderResult -> showOrders()));
    }

    private void showOrders() {
        orderRepository.getAll()
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(orderView::showOrders);
    }

    void getDeviceId(Context context) {
        Credentials credentials = new Credentials("ev7yQNcn8FPw6iFx", "9c0e44fe99e5eb73d654b2c142ecf659b0d2a179356ddeca5ad2c5182eff56d4");
        DeviceDna deviceDna = new DeviceDna(context, credentials);

        deviceDna.identifyDevice()
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(deviceId -> {
                    orderView.showDeviceId(deviceId);
                    iotApiService.registerDevice(new DeviceRegisterRequest(deviceId))
                            .subscribeOn(io())
                            .observeOn(mainThread())
                            .subscribe(orderView::showDeviceRegistration);
                });
    }
}
