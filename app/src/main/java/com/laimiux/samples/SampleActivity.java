package com.laimiux.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import rx.Observable;
import rx.Subscription;
import com.laimiux.rxnetwork.RxNetwork;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SampleActivity extends Activity {
  Button sendButton;
  private Subscription sendStateSubscription;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sample_view);


    sendButton = (Button) findViewById(R.id.send_button);

    final Observable<ButtonState> sendStateStream =
        RxNetwork.stream(this).map(new Func1<RxNetwork.State, ButtonState>() {
          @Override public ButtonState call(RxNetwork.State state) {
            if (state == RxNetwork.State.NOT_CONNECTED) {
              return new ButtonState(R.string.not_connected, false);
            }

            return new ButtonState(R.string.send, true);
          }
        });

    sendStateSubscription =
        sendStateStream.observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<ButtonState>() {
              @Override public void call(ButtonState buttonState) {
                sendButton.setText(buttonState.textId);
                sendButton.setEnabled(buttonState.isEnabled);
              }
            });
  }


  @Override protected void onDestroy() {
    sendStateSubscription.unsubscribe();
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
