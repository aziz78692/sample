package com.jakemorison.languageexchange;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;


import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jakemorison on 3/28/16.
 */
public class CallscreenActivity extends BaseActivity{

    static final String TAG = CallscreenActivity.class.getSimpleName();
    static final String CALL_START_TIME = "callStartTime";
    static final String ADDED_LISTENER = "addedListener";

    private Timer timer;
    private UpdateCallDurationTask durationTask;
    private String callId;
    private long callStart = 0;
    private boolean listener = false;
    private boolean videoAdded = false;
    private TextView callLength;
    private TextView callState;
    private TextView callerName;


    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallscreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallLength();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(CALL_START_TIME, callStart);
        savedInstanceState.putBoolean(ADDED_LISTENER, listener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        callStart = savedInstanceState.getLong(CALL_START_TIME);
        listener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callscreen);


        callLength = (TextView) findViewById(R.id.callLength);
        callerName = (TextView) findViewById(R.id.remoteCaller);
        callState = (TextView) findViewById(R.id.callState);
        Button hangup = (Button) findViewById(R.id.hangup_button);

        hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangup();
            }
        });

        callId = getIntent().getStringExtra(SinchService.CALL_ID);

        if(savedInstanceState == null){
            callStart = System.currentTimeMillis();
        }

    }

    @Override
    public void onServiceConnected(){
        Call call = getSinchServiceInterface().getCall(callId);
        if(call != null){
            if(!listener){
                call.addCallListener(new SinchCallListener());
                listener = true;
            }
        }
        else{
            Log.e(TAG, "INVALID CALLID");
            finish();
        }

        addCall();
    }

    private void addCall(){
        if(getSinchServiceInterface() == null){
            return;
        }

        Call call = getSinchServiceInterface().getCall(callId);
        if(call != null){
            callerName.setText(call.getRemoteUserId());
            callState.setText(call.getState().toString());
            if(call.getState() == CallState.ESTABLISHED){
                addVideoView();
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        durationTask.cancel();
        timer.cancel();
        endVideoView();
    }

    @Override
    public void onStart(){
        super.onStart();
        timer = new Timer();
        durationTask = new UpdateCallDurationTask();
        timer.schedule(durationTask, 0, 500);
        addCall();
    }

    public void hangup(){
        Call call = getSinchServiceInterface().getCall(callId);
        if(call != null){
            call.hangup();
        }
        finish();
    }

    private String formatTime(long milliseconds){
        long seconds = milliseconds/1000;
        long minutes = seconds/60;
        long seconds2 = seconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds2);
    }

    private void updateCallLength(){
        if(callStart > 0){
            callLength.setText(formatTime(System.currentTimeMillis()-callStart));
        }
    }

    private void addVideoView(){
        if(videoAdded || getSinchServiceInterface() ==null){
            return;
        }

        final VideoController videoController = getSinchServiceInterface().getVideoController();
        if(videoController != null){
            final RelativeLayout localView = (RelativeLayout) findViewById(R.id.localView);
            localView.addView(videoController.getLocalView());
            localView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoController.toggleCaptureDevicePosition();
                }
            });

            LinearLayout view = (LinearLayout) findViewById(R.id.videoView);
            view.addView(videoController.getRemoteView());
            videoAdded = true;

        }
    }

    private void endVideoView(){
        if(getSinchServiceInterface() == null){
            return;
        }

        VideoController videoController = getSinchServiceInterface().getVideoController();

        if(videoController != null){
            LinearLayout view = (LinearLayout) findViewById(R.id.videoView);
            view.removeView(videoController.getRemoteView());

            RelativeLayout localView = (RelativeLayout) findViewById(R.id.localView);
            localView.removeView(videoController.getLocalView());
            videoAdded = false;
        }
    }


    @Override
    public void onBackPressed(){

    }


    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause callEndCause = call.getDetails().getEndCause();
            Log.d(TAG, "CALL ENDED: " + callEndCause.toString());


            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String message = "CALL ENDED: " + call.getDetails().toString();
            Toast.makeText(CallscreenActivity.this, message, Toast.LENGTH_LONG).show();

            hangup();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");

            callState.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = getSinchServiceInterface().getAudioController();
            audioController.enableSpeaker();
            callStart = System.currentTimeMillis();
            Log.d(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");

        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs){

        }


        @Override
        public void onVideoTrackAdded(Call call) {
            Log.d(TAG, "video track added");
            addVideoView();
        }
    }
}
