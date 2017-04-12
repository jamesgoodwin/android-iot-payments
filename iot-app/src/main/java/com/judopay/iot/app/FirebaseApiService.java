package com.judopay.iot.app;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.judopay.iot.app.device.Device;
import com.judopay.iot.app.order.Order;

import rx.AsyncEmitter;
import rx.Completable;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static java.util.UUID.randomUUID;
import static rx.Completable.complete;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class FirebaseApiService implements ApiService {

    private DatabaseReference databaseReference;

    public FirebaseApiService() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public Completable addDeviceOrder(Order order, String deviceId) {
        return Completable.defer(() -> {
            DatabaseReference orderRef = databaseReference.child("devices")
                    .child(deviceId)
                    .child("orders")
                    .child(randomUUID().toString());

            orderRef.setValue(order);
            orderRef.push();

            return complete();
        });
    }

    @Override
    public Completable addDevice(Device device) {
        DatabaseReference devices = databaseReference.child("devices");

        return dbObserver(devices)
                .subscribeOn(io())
                .observeOn(mainThread())
                .flatMap((Func1<DataSnapshot, Observable<Void>>) dataSnapshot -> {
                    if (!dataSnapshot.hasChild(device.getId())) {
                        DatabaseReference deviceReference = devices.child(device.getId());
                        deviceReference.setValue(device);

                        deviceReference.push();
                    }
                    return null;
                }).toCompletable();
    }

    private Observable<DataSnapshot> dbObserver(DatabaseReference reference) {
        return Observable.fromAsync(asyncEmitter -> {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    asyncEmitter.onNext(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            reference.addListenerForSingleValueEvent(valueEventListener);
        }, AsyncEmitter.BackpressureMode.LATEST);
    }
}
