package com.laimiux.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.laimiux.rxnetwork.RxNetwork;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class SampleActivity extends Activity {
    Button sendButton;
    private CompositeDisposable sendStateSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_view);


        sendButton = (Button) findViewById(R.id.send_button);

        final Observable<ButtonState> sendStateStream =
                RxNetwork.stream(this).map(new Function<Boolean, ButtonState>() {
                    @Override
                    public ButtonState apply(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            return new ButtonState(R.string.not_connected, false);
                        }

                        return new ButtonState(R.string.send, true);
                    }
                });
        sendStateSubscription = new CompositeDisposable();
        sendStateSubscription.add(sendStateStream.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ButtonState>() {
                    @Override
                    public void accept(ButtonState buttonState) throws Exception {
                        sendButton.setText(buttonState.textId);
                        sendButton.setEnabled(buttonState.isEnabled);
                    }
                }));

    }


    @Override
    protected void onDestroy() {
        sendStateSubscription.dispose();
        sendStateSubscription = null;

        super.onDestroy();
    }

    static class ButtonState {
        final int textId;
        final boolean isEnabled;

        public ButtonState(int textId, boolean isEnabled) {
            this.textId = textId;
            this.isEnabled = isEnabled;
        }
    }
}
