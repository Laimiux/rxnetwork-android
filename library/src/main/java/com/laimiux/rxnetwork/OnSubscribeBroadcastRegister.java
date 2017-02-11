package com.laimiux.rxnetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


class OnSubscribeBroadcastRegister implements ObservableOnSubscribe<Intent> {

    private final Context context;
    private final IntentFilter intentFilter;
    private final String broadcastPermission;
    private final Handler schedulerHandler;

    public OnSubscribeBroadcastRegister(Context context, IntentFilter intentFilter, String broadcastPermission, Handler schedulerHandler) {
        this.context = context;
        this.intentFilter = intentFilter;
        this.broadcastPermission = broadcastPermission;
        this.schedulerHandler = schedulerHandler;
    }


    @Override
    public void subscribe(final ObservableEmitter<Intent> e) throws Exception {
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                e.onNext(intent);
            }
        };
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(new Disposable() {
            @Override
            public void dispose() {
                context.unregisterReceiver(broadcastReceiver);
            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        context.registerReceiver(broadcastReceiver, intentFilter, broadcastPermission, schedulerHandler);
    }
}