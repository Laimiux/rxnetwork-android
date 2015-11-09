package com.laimiux.rxnetwork;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import rx.Observable;
import rx.functions.Func1;


/**
 * Observe the network change state.
 * <p/>
 * To use this, you need to add this code to AndroidManifest.xml
 * <p/>
 * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}
 * <p/>
 * <pre>
 */
public class RxNetwork {
    public enum State {
        NOT_CONNECTED,
        MOBILE,
        WIFI
    }

    private RxNetwork() {
        // No instances
    }

    /**
     * Helper function that returns the connectivity state
     *
     * @param context Context
     * @return Connectivity State
     */
    public static State getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return State.WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return State.MOBILE;
        }
        return State.NOT_CONNECTED;
    }

    public static Observable<State> stream(Context context) {
        final Context applicationContext = context.getApplicationContext();
        final IntentFilter action = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        final Observable<State> stateStream =
                ContentObservable.fromBroadcast(context, action).map(new Func1<Intent, State>() {
                    @Override public State call(Intent intent) {
                        return getConnectivityStatus(applicationContext);
                    }
                });

        return stateStream.startWith(getConnectivityStatus(applicationContext));
    }
}
