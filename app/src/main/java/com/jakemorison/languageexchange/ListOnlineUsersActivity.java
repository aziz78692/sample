
package com.jakemorison.languageexchange;
/*
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by jakemorison on 3/26/16.
 *//*


public class ListOnlineUsersActivity extends BaseActivity{
    private Pubnub pubnub;
    private ArrayList users;
    private JSONArray hereNowUuids;
    public static final String APP_KEY = "26dedbd2-378d-45c4-bb2b-314bbf274f77";
    private static final String APP_SECRET = "RI0qT1GwhEKThuVkSipyeg==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    private SinchClient mSinchClient;
    public String username;
    public String nativeChoice;
    public String foreignChoice;
    public String mode = "learn";
    public Button switchMode;

    private void start(String userName) {
        if (mSinchClient == null) {
            //mUserId = userName;
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.startListeningOnActiveConnection();

            mSinchClient.addSinchClientListener(new SinchClientListener() {
                public void onClientStarted(SinchClient client) {
                }

                public void onClientStopped(SinchClient client) {
                }

                public void onClientFailed(SinchClient client, SinchError error) {
                }

                public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) {
                }

                public void onLogMessage(int level, String area, String message) {
                }

            });
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            mSinchClient.start();
        }
    }

    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listonlineusers_activity);

        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(!extrasBundle.isEmpty()){
            username = extrasBundle.getString("username");
            nativeChoice = extrasBundle.getString("nativeChoice");
            foreignChoice = extrasBundle.getString("foreignChoice");
        }
*/
/*

        switchMode = (Button) findViewById(R.id.switchModeButton);
        switchMode.setEnabled(true);
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("learn")){
                    mode = "teach";
                }
                else if (mode.equals("teach")){
                    mode = "learn";
                }
            }
        });
*//*


        pubnub = new Pubnub("pub-c-64ec5b97-6eef-4778-a9df-6a63f7b22e0f", "sub-c-87213770-f39d-11e5-9086-02ee2ddab7fe");
        pubnub.setUUID(RememberStrings.username);

        //Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), nativeChoice, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), foreignChoice, Toast.LENGTH_LONG).show();

    }




    @Override
    protected void onResume(){
        super.onResume();
        users = new ArrayList<String>();
        final ListView usersListView = (ListView)findViewById(R.id.usersListView);
        final ArrayAdapter usersArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_list_item, users);
        usersListView.setAdapter(usersArrayAdapter);

        pubnub.hereNow("calling_channel", new Callback() {
            public void successCallback(String channel, Object o) {
                try {
                    JSONObject hereNowResponse = new JSONObject(o.toString());
                    hereNowUuids = new JSONArray(hereNowResponse.get("uuids").toString());
                } catch (JSONException e) {
                    Log.d("JSONException", e.toString());
                }

                String currentUuid;

*/
/*                //if no other users, show error message
                if(hereNowUuids.length() < 2){
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Error...");
                    alertDialog.setMessage("No other users online, try again later");
                    alertDialog.setButton("Return to search", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ListOnlineUsersActivity.this, SearchActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.show();
                }*//*


                for (int i = 0; i < hereNowUuids.length(); i++) {
                    try {
                        currentUuid = hereNowUuids.get(i).toString();
                        if (!currentUuid.equals(pubnub.getUUID())) {
                            users.add(currentUuid);
                            ListOnlineUsersActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    usersArrayAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString());
                    }
                }
            }

            public void errorCallback(String channel, PubnubError error) {
                Log.d("PubnubError", error.toString());
            }
        });


        //subscribe to calling_channel
        try {
            pubnub.subscribe("calling_channel", new Callback() {
            });

        }
        catch(PubnubException e){
            Log.d("PubnubException", e.toString());
        }

        // listen to channel to know when users join/leave channel

        try {
            pubnub.presence("calling_channel", new Callback(){

                @Override
                public void successCallback(String channel, Object message) {
                    try {
                        JSONObject jsonMessage = new JSONObject(message.toString());
                        String action = jsonMessage.get("action").toString();
                        String uuid = jsonMessage.get("uuid").toString();

                        if(!uuid.equals(pubnub.getUUID())) {
                            if(action.equals("join")) {
                                users.add(uuid);
                                ListOnlineUsersActivity.this.runOnUiThread(new Runnable(){
                                    @Override
                                public void run() {
                                        usersArrayAdapter.notifyDataSetChanged();
                                    }
                                });

                            }

                            else if (action.equals("leave")){
                                for (int i = 0; i < users.size(); i++){
                                    if(users.get(i).equals(uuid)){
                                        users.remove(i);
                                        ListOnlineUsersActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                usersArrayAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }

                    catch (JSONException e) {
                        Log.d("JSONException", e.toString());
                    }
                }
            });
        }
        catch (PubnubException e){
            Log.d("PubnubException", e.toString());
        }
        usersListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                userChosen(view, usersListView.getItemAtPosition(position).toString());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        pubnub.unsubscribe("calling_channel");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;//return true so that the menu pop up is opened

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.logout:
                Intent intent = new Intent(ListOnlineUsersActivity.this, LogoutActivity.class);
                startActivity(intent);

        }
        return true;
    }

    public void userChosen(final View view, final String username) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.call:
                        call(view, username);
                    case R.id.cancel:
                        Intent intent = new Intent(ListOnlineUsersActivity.this, ListOnlineUsersActivity.class);
                        startActivity(intent);
                }
                return true;
            }
        });
        menu.inflate(R.menu.popup_menu);
        menu.show();
    }

    public void call(View view, String username){
        CallClient callClient = mSinchClient.getCallClient();
        Call call = callClient.callUser(username);

        String callId = call.getCallId();

        Intent intent = new Intent(ListOnlineUsersActivity.this, CallscreenActivity.class);
        intent.putExtra(SinchService.CALL_ID, callId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Are you sure you want to return to the language preferences?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(ListOnlineUsersActivity.this, LoginActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
*/


import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.pubnub.api.*;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ListOnlineUsersActivity extends BaseActivity {

    static final String JSON_EXCEPTION = "JSON Exception";
    static final String PUBNUB_EXCEPTION = "Pubnub Exception";


    private Pubnub pubnub;
    private ArrayList users;
    private JSONArray hereNowUuids;
    private SinchClient sinchClient;
    private Button pickupButton;
    private Button hangupButton;
    private Call currentCall = null;

    public String username = "test";
    public static final String APP_KEY = "26dedbd2-378d-45c4-bb2b-314bbf274f77";
    private static final String APP_SECRET = "RI0qT1GwhEKThuVkSipyeg==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listonlineusers_activity);

        username = getIntent().getExtras().getString("username");



        pubnub = new Pubnub("pub-c-64ec5b97-6eef-4778-a9df-6a63f7b22e0f", "sub-c-87213770-f39d-11e5-9086-02ee2ddab7fe");
        pubnub.setUUID(username);

        sinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(username)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT).build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();

        sinchClient.addSinchClientListener(new SinchClientListener() {
            public void onClientStarted(SinchClient client) {
            }

            public void onClientStopped(SinchClient client) {
            }

            public void onClientFailed(SinchClient client, SinchError error) {
            }

            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) {
            }

            public void onLogMessage(int level, String area, String message) {
            }

        });
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
        sinchClient.start();


    }

    @Override
    public void onResume() {
        super.onResume();

        users = new ArrayList<String>();
        final ListView usersListView = (ListView) findViewById(R.id.usersListView);
        final ArrayAdapter usersArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_list_item, users);
        usersListView.setAdapter(usersArrayAdapter);


        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                if (currentCall == null) {
                    currentCall = sinchClient.getCallClient().callUser(users.get(i).toString());
                    currentCall.addCallListener(new SinchCallListener());
                    hangupButton.setText("Hang Up Call with " + users.get(i));
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Can't call " + users.get(i) + " while on another call.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        pubnub.hereNow("calling_channel", new Callback() {
            public void successCallback(String channel, Object response) {
                try {
                    JSONObject hereNowResponse = new JSONObject(response.toString());
                    hereNowUuids = new JSONArray(hereNowResponse.get("uuids").toString());
                } catch (JSONException e) {
                    Log.d(JSON_EXCEPTION, e.toString());
                }

                String currentUuid;
                for (int i = 0; i < hereNowUuids.length(); i++) {
                    try {
                        currentUuid = hereNowUuids.get(i).toString();
                        if (!currentUuid.equals(pubnub.getUUID())) {
                            users.add(currentUuid);
                            ListOnlineUsersActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    usersArrayAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.d(JSON_EXCEPTION, e.toString());
                    }
                }
            }

            public void errorCallback(String channel, PubnubError e) {
                Log.d("PubnubError", e.toString());
            }
        });

        try {
            pubnub.subscribe("calling_channel", new Callback() {
            });
        } catch (PubnubException e) {
            Log.d(PUBNUB_EXCEPTION, e.toString());
        }

        try {
            pubnub.presence("calling_channel", new Callback() {

                @Override
                public void successCallback(String channel, Object message) {
                    try {
                        JSONObject jsonMessage = new JSONObject(message.toString());
                        String action = jsonMessage.get("action").toString();
                        String uuid = jsonMessage.get("uuid").toString();

                        if (!uuid.equals(pubnub.getUUID())) {
                            if (action.equals("join")) {
                                users.add(uuid);
                                ListOnlineUsersActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        usersArrayAdapter.notifyDataSetChanged();
                                    }
                                });
                            } else if (action.equals("leave")) {
                                for (int i = 0; i < users.size(); i++) {
                                    if (users.get(i).equals(uuid)) {
                                        users.remove(i);
                                        ListOnlineUsersActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                usersArrayAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(JSON_EXCEPTION, e.toString());
                    }
                }
            });
        } catch (PubnubException e) {
            Log.d(PUBNUB_EXCEPTION, e.toString());
        }

        usersListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                userChosen(view, usersListView.getItemAtPosition(position).toString());
            }
        });
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call call) {
            currentCall = null;
            hangupButton.setText("No call to hang up right now...");
            pickupButton.setText("No call to pick up right now...");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call call) {
            //hangupButton.setText("Hang up call with " + call.getRemoteUserId());
            //pickupButton.setText("No call to pick up right now...");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call call) {
            hangupButton.setText("Ringing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            if (currentCall == null) {
                currentCall = incomingCall;
                currentCall.addCallListener(new SinchCallListener());
                //pickupButton.setText("Pick up call from " + incomingCall.getRemoteUserId());
                //hangupButton.setText("Ignore call from " + incomingCall.getRemoteUserId());
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        pubnub.unsubscribe("calling_channel");
    }

    public void userChosen(final View view, final String username) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.call:
                        call(view, username);
                    case R.id.cancel:
                        /*Intent intent = new Intent(ListOnlineUsersActivity.this, ListOnlineUsersActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);*/
                }
                return true;
            }
        });
        menu.inflate(R.menu.popup_menu);
        menu.show();
    }

    public void call(View view, String otherUser){
        //CallClient callClient = sinchClient.getCallClient();
        Call call = getSinchServiceInterface().callUserVideo(otherUser);

        String callId = call.getCallId();

        Intent intent = new Intent(ListOnlineUsersActivity.this, CallscreenActivity.class);
        intent.putExtra(SinchService.CALL_ID, callId);
        startActivity(intent);
    }


}
