package com.jakemorison.languageexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

/**
 * Created by jakemorison on 3/24/16.
 */
public class LogoutActivity extends BaseActivity{

    private TextView info;
    private LoginButton loginButton;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_login_activity);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        //Check if user is currently logged in
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null){
            //Logged in so show the login button
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//log out
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(LogoutActivity.this, FacebookLoginActivity.class);
                    startActivity(intent);

                }
            });
        }
    }


}