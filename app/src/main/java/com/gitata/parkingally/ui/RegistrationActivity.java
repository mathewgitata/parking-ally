package com.gitata.parkingally.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gitata.parkingally.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.registration_btn_register)
    Button signUpBtn;
    @BindView(R.id.registration_et_email)
    EditText etEmail;
    @BindView(R.id.registration_et_first_name)
    EditText etFirstName;
    @BindView(R.id.registration_et_last_name)
    EditText etLastName;
    @BindView(R.id.registration_et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.registration_et_password)
    EditText etPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        signUpBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registration_btn_register:
                //validation checks
                String email = etEmail.getText().toString().trim();
                String first_name = etFirstName.getText().toString().trim();
                String last_name = etLastName.getText().toString().trim();
                String phone_number = etPhoneNumber.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    etEmail.setError("Email required");
                    etEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter a valid email address");
                    etEmail.requestFocus();
                }
                if (first_name.isEmpty()) {
                    etFirstName.setError("First name required");
                    etFirstName.requestFocus();
                    return;
                }
                if (last_name.isEmpty()) {
                    etLastName.setError("Last name required");
                    etLastName.requestFocus();
                    return;
                }
                if (phone_number.isEmpty()) {
                    etPhoneNumber.setError("Phone number required");
                    etPhoneNumber.requestFocus();
                    return;
                }
                if (phone_number.length() != 10) {
                    etPhoneNumber.setError("Enter a valid phone number");
                    etPhoneNumber.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    etPassword.setError(getString(R.string.input_error_password));
                    etLastName.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    etPassword.setError("Password should be at least 6 characters long");
                    etPassword.requestFocus();
                    return;
                }
        }
    }
}
