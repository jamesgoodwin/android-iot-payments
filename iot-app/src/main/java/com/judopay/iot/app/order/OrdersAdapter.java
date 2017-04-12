package com.judopay.iot.app.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.judopay.iot.app.R;

import java.util.List;

class OrdersAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private final List<Order> orders;

    OrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.view_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.setOrder(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}