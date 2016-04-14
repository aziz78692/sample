package com.jakemorison.languageexchange;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by jakemorison on 3/24/16.
 */
public class FacebookLoginActivity extends BaseActivity {
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String username;
    public Button altLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.fb_login_activity);
        info = (TextView) findViewById(R.id.info);

        altLogin = (Button) findViewById(R.id.altLogin);
        altLogin.setEnabled(true);
        altLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = "test user";
                String userID = "test_id";


                if(username != null) {
                    Intent intent = new Intent(FacebookLoginActivity.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }


        });


        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                username = profile.getName();
                String userID = profile.getId();


                if(username != null) {
                    Intent intent = new Intent(FacebookLoginActivity.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }


            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setData(){
        if(AccessToken.getCurrentAccessToken() != null){
            final GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            if(AccessToken.getCurrentAccessToken()!=null){
                                try {
                                    username = response.getJSONObject().getString("first_name")+ " " + response.getJSONObject().getString("last_name");
                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                }

                            }

                        }
                    });
        }
    }
}

   /* private void setFacebookData(final LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try{
                            Log.i("Response", response.toString());

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");
                            String bday= response.getJSONObject().getString("birthday");

                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link",link);
                            if (Profile.getCurrentProfile()!=null)
                            {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }

                            RememberStrings.username = firstName + " " + lastName;


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }*/

