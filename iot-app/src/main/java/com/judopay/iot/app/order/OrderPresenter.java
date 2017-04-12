package com.judopay.iot.app.order;

import android.content.Context;
import android.text.TextUtils;

import com.judopay.DeviceDna;
import com.judopay.devicedna.Credentials;
import com.judopay.iot.app.ApiService;
import com.judopay.iot.app.FirebaseApiService;
import com.judopay.iot.app.device.Device;

import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class OrderPresenter {

    private final ApiService apiService;
    private final OrderRepository orderRepository;
    private final OrderView orderView;

    private String deviceId;

    OrderPresenter(OrderView orderView, OrderRepository orderRepository) {
        this.orderView = orderView;
        this.orderRepository = orderRepository;
        this.apiService = new FirebaseApiService();
    }

    public void order(Order order) {
        if(!TextUtils.isEmpty(deviceId)) {
            orderRepository.add(order)
                    .subscribeOn(io())
                    .observeOn(mainThread())
                    .subscribe(() -> apiService.addDeviceOrder(order, deviceId)
                            .subscribeOn(io())
                            .observeOn(mainThread())
                            .subscribe(this::showOrders));
        }
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
                    this.deviceId = deviceId;
                    orderView.showDeviceId(deviceId);
                    apiService.addDevice(new Device(deviceId, "Beer fridge"))
                            .subscribeOn(io())
                            .observeOn(mainThread())
                            .subscribe();
                });
    }
}
