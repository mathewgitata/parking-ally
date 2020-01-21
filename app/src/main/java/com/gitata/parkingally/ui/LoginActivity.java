package com.gitata.parkingally.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gitata.parkingally.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = "FB_LOG_IN";

    @BindView(R.id.btnLogin)
    Button loginBtn;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "Signed in: " + user.getUid());
                } else {
                    Log.d(TAG, "Currently Signed out");
                }
            }
        };

        loginBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    etEmail.setError("Email required");
                    etEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("You must provide email");
                    etEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    etPassword.setError("You must provide password");
                    etPassword.requestFocus();
                    return;
                }
                logInUser(email, password);
        }
    }

    private void logInUser(String email, String password) {
        final ProgressDialog mProgressDialog = new ProgressDialog(this, R.style.AppCompatAlerDialogStyle);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if (!task.isSuccessful()) {
                    Log.d(TAG, task.getException().getLocalizedMessage());
                } else {
                    hideKeyboard(LoginActivity.this);
                    Intent toHomePage = new Intent(LoginActivity.this, ParkingLotListActivity.class);
                    startActivity(toHomePage);
                }
            }
        })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        hideKeyboard(LoginActivity.this);
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Invalid email/password combination", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else if (e instanceof FirebaseAuthInvalidUserException) {
                        hideKeyboard(LoginActivity.this);
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "No account with that email exists", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Register", v -> {
                            Intent toRegister = new Intent(LoginActivity.this, RegistrationActivity.class);
                            startActivity(toRegister);
                        });
                        snackbar.show();
                    } else {
                        hideKeyboard(LoginActivity.this);
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Error: " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
    }


    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

