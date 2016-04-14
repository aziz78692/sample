package com.jakemorison.languageexchange;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jakemorison on 3/24/16.
 */
public class IncomingCallScreenActivity extends BaseActivity {
    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private String callId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming);

        Button answerButton = (Button) findViewById(R.id.answerButton);
        answerButton.setOnClickListener(clickListener);

        Button declineButton = (Button) findViewById(R.id.declineButton);
        declineButton.setOnClickListener(clickListener);

        callId = getIntent().getStringExtra(SinchService.CALL_ID);

    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(callId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = (TextView) findViewById(R.id.remoteUser);
            remoteUser.setText(call.getRemoteUserId());

        } else {
            Log.e(TAG, "CALLID ERROR");
            finish();
        }
    }

    private void acceptCall() {
        Call call = getSinchServiceInterface().getCall(callId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallscreenActivity.class);
            intent.putExtra(SinchService.CALL_ID, callId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineCall(){
        Call call = getSinchServiceInterface().getCall(callId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "ERROR: " + cause.toString());
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }

        @Override
        public void onVideoTrackAdded(Call call) {
        }
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    acceptCall();
                    break;
                case R.id.declineButton:
                    declineCall();
                    break;
            }
        }
    };

}
