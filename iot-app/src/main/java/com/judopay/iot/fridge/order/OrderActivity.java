package com.judopay.iot.fridge.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.judopay.iot.fridge.R;
import com.judopay.iot.fridge.device.DeviceRegisterResult;

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

        initializeOrderButton();
        getDeviceId();
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
        if("Registered".equals(deviceRegisterResult.getStatus())) {
            deviceRegistrationStatusText.setText("Yes");
        } else {
            deviceRegistrationStatusText.setText("No");
        }
    }

    private void initializeOrderButton() {
        findViewById(R.id.order_button)
                .setOnClickListener(view -> orderPresenter.order(new Order("Beer", "20.00")));
    }

    private void getDeviceId() {
        orderPresenter.getDeviceId(this);
    }
}
