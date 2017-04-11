package com.judopay.iot.fridge.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;
import com.google.android.things.contrib.driver.ht16k33.Ht16k33;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.judopay.iot.fridge.R;
import com.judopay.iot.fridge.device.DeviceRegisterResult;

import java.io.IOException;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements OrderView {

    private OrderPresenter orderPresenter;
    private RecyclerView ordersRecyclerView;
    private TextView deviceIdText;
    private TextView deviceRegistrationStatusText;

    private OrderService orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        this.orderPresenter = new OrderPresenter(this, orderRepository);

        setContentView(R.layout.order_activity);

        deviceIdText = (TextView) findViewById(R.id.device_id_text);
        deviceRegistrationStatusText = (TextView) findViewById(R.id.device_registration_status_text);
        ordersRecyclerView = (RecyclerView) findViewById(R.id.orders_recycler_view);

        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderService = new OrderService(this, orderRepository);
        orderService.start();

        try {
            initializeOrderButton();
            initializeScreen("BEER");
        } catch (Exception e) {
            Log.e("Judo", "Error initializing view", e);
        }

        getDeviceId();
    }

    private void initializeScreen(String message) throws IOException {
        AlphanumericDisplay segment = RainbowHat.openDisplay();
        segment.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX);
        segment.display(message);
        segment.setEnabled(true);
        segment.close();
    }

    @Override
    public void onStop() {
        super.onStop();
        orderService.stop();
    }

    @Override
    public void showOrders(List<Order> orders) {
        ordersRecyclerView.setAdapter(new OrdersAdapter(orders));
    }

    @Override
    public void showDeviceId(String deviceId) {
        deviceIdText.setText(deviceId);
    }

    @Override
    public void showDeviceRegistration(DeviceRegisterResult deviceRegisterResult) {
        if ("Registered".equals(deviceRegisterResult.getStatus())) {
            deviceRegistrationStatusText.setText("Yes");
        } else {
            deviceRegistrationStatusText.setText("No");
        }
    }

    private void initializeOrderButton() throws IOException {
        Button button = RainbowHat.openButton(RainbowHat.BUTTON_A);

        button.setOnButtonEventListener((button1, pressed) -> {
            if (pressed) {
                orderBeer();
            }
        });
    }

    private void orderBeer() {
        Order beer = new Order("Beer", "20.00");
        orderPresenter.order(beer);
    }

    private void getDeviceId() {
        orderPresenter.getDeviceId(this);
    }
}
