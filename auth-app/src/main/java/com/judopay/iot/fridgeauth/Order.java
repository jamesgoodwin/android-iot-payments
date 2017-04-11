package com.judopay.iot.fridgeauth;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

class Order implements Parcelable {

    boolean isAuthorized() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Order() {
    }

    protected Order(Parcel in) {
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    String getSummary() {
        return String.format(Locale.ENGLISH, "Beer %s", "£19.99");
    }

    String getDescription() {
        return "Order for 16 bottles of Kronenbourg, 16 bottles of Stella";
    }
}
