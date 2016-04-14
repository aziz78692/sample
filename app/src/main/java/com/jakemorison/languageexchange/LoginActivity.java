package com.jakemorison.languageexchange;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jakemorison on 3/31/16.
 */
public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener{
    private Button mLoginButton;
    private Button mLogoutButton;
    private Spinner nativeLangSpin;
    private Spinner foreignLangSpin;
    public String username;
    private ProgressDialog dialogSpin;
    private String nativeChoice;
    private String foreignChoice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(!extrasBundle.isEmpty()){
            username = extrasBundle.getString("username");
        }

        //Toast.makeText(this, username, Toast.LENGTH_SHORT).show();;

        nativeLangSpin = (Spinner) findViewById(R.id.spinner);
        foreignLangSpin = (Spinner) findViewById(R.id.spinnerTwo);

        mLoginButton = (Button) findViewById(R.id.loginButton);

        mLoginButton.setEnabled(true);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();
            }


        });

        mLogoutButton = (Button) findViewById(R.id.logoutButton);
        mLogoutButton.setEnabled(true);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutClicked();
            }


        });


        List<String> categories = new ArrayList<>();
        categories.add("Choose an option...");
        categories.add("English");
        categories.add("Spanish");
        categories.add("French");
        categories.add("Italian");
        categories.add("German");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        nativeLangSpin.setAdapter(dataAdapter);


        List<String> categories2 = new ArrayList<>();
        categories2.add("Choose an option...");
        categories2.add("English");
        categories2.add("Spanish");
        categories2.add("French");
        categories2.add("Italian");
        categories2.add("German");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        foreignLangSpin.setAdapter(dataAdapter2);
    }


    @Override
    protected void onServiceConnected(){
        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);

    }

    @Override
    protected void onPause(){
        if(dialogSpin!=null){
            dialogSpin.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError e) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        if(dialogSpin != null) {
            dialogSpin.dismiss();
        }
    }

    @Override
    public void onStarted(){
        nextActivity();
    }

    private void loginClicked(){
        nativeChoice = nativeLangSpin.getSelectedItem().toString();
        foreignChoice = foreignLangSpin.getSelectedItem().toString();
        if(nativeChoice.equalsIgnoreCase("Choose an option...") || foreignChoice.equalsIgnoreCase("Choose an option...") || foreignChoice.equals(nativeChoice)){
            Toast.makeText(this, "Please make a valid choice", Toast.LENGTH_SHORT).show();;
            return;
        }

        if(!getSinchServiceInterface().isStarted()) {

            getSinchServiceInterface().startClient(username);
            showSpinner();
        }

        else{
            nextActivity();
        }
    }

    private void logoutClicked(){
        Intent intent = new Intent(this, LogoutActivity.class);
        startActivity(intent);
    }

    private void nextActivity(){
        Intent intent = new Intent(this, ListOnlineUsersActivity.class);
        intent.putExtra("username", username);
        //intent.putExtra("foreignChoice", foreignChoice);
        //intent.putExtra("nativeChoice", nativeChoice);
        startActivity(intent);
    }

    private void showSpinner(){
        dialogSpin = new ProgressDialog(this);
        dialogSpin.setMessage("Please wait");
        dialogSpin.show();
    }
}
