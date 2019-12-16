package com.gitata.parkingally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandingPageActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @OnClick(R.id.landing_btn_sign_up)
    public void onSignUpButtonClick() {
        Log.i(TAG, "SignUpClick");
        //TODO: Redirect user to Registration Page
    }

    @OnClick(R.id.landing_btn_login)
    public void onLoginButtonClick() {
        Log.i(TAG, "LoginClick");
        //TODO: Redirect user to Registration Page
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);
    }
}
