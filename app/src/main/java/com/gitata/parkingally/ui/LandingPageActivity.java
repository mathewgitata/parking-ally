package com.gitata.parkingally.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.gitata.parkingally.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandingPageActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.landing_btn_sign_up)
    public void onSignUpButtonClick() {
        Intent toSignUp = new Intent(LandingPageActivity.this, RegistrationActivity.class);
        startActivity(toSignUp);
    }

    @OnClick(R.id.landing_btn_login)
    public void onLoginButtonClick() {
        Intent toSignUp = new Intent(LandingPageActivity.this, LoginActivity.class);
        startActivity(toSignUp);
    }
}
