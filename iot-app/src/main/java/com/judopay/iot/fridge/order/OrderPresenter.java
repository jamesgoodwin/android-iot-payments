package com.judopay.iot.fridge.order;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.judopay.DeviceDna;
import com.judopay.devicedna.Credentials;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class OrderPresenter {

    private final IotApiService iotApiService;
    private final OrderRepository orderRepository;
    private final OrderView orderView;

    OrderPresenter(OrderView orderView, OrderRepository orderRepository) {
        this.orderView = orderView;
        this.orderRepository = orderRepository;

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://172.27.132.92:3000/")
                .client(new OkHttpClient.Builder().build())
                .build();

        this.iotApiService = retrofit.create(IotApiService.class);
    }

    public void order(Order order) {
        orderRepository.add(order)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(() -> iotApiService.order(order)
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .subscribe(orderResult -> showOrders(), throwable -> Log.e("Judo", "Error creating order", throwable)));
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
//                    iotApiService.registerDevice(new DeviceRegisterRequest(deviceId))
//                            .subscribeOn(io())
//                            .observeOn(mainThread())
//                            .subscribe(orderView::showDeviceRegistration,
//                                    throwable -> Log.e("Judo", "Error getting device ID", throwable));
                });
    }
}
