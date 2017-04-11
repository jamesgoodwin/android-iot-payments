package com.judopay.iot.fridgeauth;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import rx.Observable;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.judopay.iot.fridgeauth.AuthorizeActivity.EXTRA_ORDER;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class AuthorizationService extends IntentService {

    private final AuthApiService authApiService;

    public AuthorizationService() {
        super("AuthorizationService");
        this.authApiService = new MockAuthApiService();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        authApiService.getOrders()
                .subscribeOn(io())
                .observeOn(mainThread())
                .toObservable()
                .flatMap(Observable::from)
                .filter(order -> !order.isAuthorized())
                .subscribe(this::sendNotification);
    }

    private void sendNotification(Order order) {
        Intent intent = new Intent(getApplicationContext(), AuthorizeActivity.class);

        intent.putExtra(EXTRA_ORDER, order);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 101, intent, FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(order.getSummary())
                .setContentText(order.getDescription())
                .setSmallIcon(R.drawable.ic_lock_outline_black)
                .setPriority(Notification.PRIORITY_MAX)
                .setLocalOnly(true)
                .addAction(new NotificationCompat.Action(R.drawable.ic_lock_outline_black, "Authorize", pendingIntent))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}