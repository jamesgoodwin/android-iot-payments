package com.judopay.iot.app.order;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.judopay.iot.app.R;

class OrderViewHolder extends RecyclerView.ViewHolder {

    OrderViewHolder(View itemView) {
        super(itemView);
    }

    public void setOrder(Order order) {
        TextView orderDescriptionText = (TextView) itemView.findViewById(R.id.order_description_text);

        orderDescriptionText.setText(order.getDescription());

        TextView orderTotalText = (TextView) itemView.findViewById(R.id.order_total_text);

        orderTotalText.setText(order.getTotal());
    }
}