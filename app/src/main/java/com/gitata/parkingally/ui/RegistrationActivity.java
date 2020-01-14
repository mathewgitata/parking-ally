package com.gitata.parkingally.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.gitata.parkingally.R;
import com.gitata.parkingally.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "FB_REGISTER";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "Signed in: " + user.getUid());
            } else {
                Log.d(TAG, "Currently Signed out");
            }
        };

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
                    return;
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
                registerUser(email, first_name, last_name, phone_number, password);
        }
    }

    private void registerUser(String email, final String firstName, final String lastName,
                              final String phoneNumber, String password) {
        final ProgressBar mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (!task.isSuccessful()) {
                    Log.d(TAG, task.getException().getLocalizedMessage());
                } else {

                    final FirebaseUser newUser = task.getResult().getUser();
                    final User user = new User(newUser.getUid(), newUser.getEmail(), firstName, lastName, phoneNumber);
                    UserProfileChangeRequest updateAccount = new UserProfileChangeRequest.Builder()
                            .build();

                    newUser.updateProfile(updateAccount)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    ProgressBar mProgressBar = findViewById(R.id.progressBar);
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                        createUserInFirebaseDb(newUser.getUid(), newUser.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber());
                                    } else {
                                        Log.d(TAG, "Error: " + task.getException().getLocalizedMessage());
                                    }
                                }
                            });
                }
            }
        }).addOnFailureListener(e -> {
            if (e instanceof FirebaseAuthUserCollisionException) {
                hideKeyboard(RegistrationActivity.this);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "This email is already in use", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                hideKeyboard(RegistrationActivity.this);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Error: " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

    private void createUserInFirebaseDb(String userId, String email, String firstName, String lastName, String phoneNumber) {
        DatabaseReference mUsersDBref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(userId, email, firstName, lastName, phoneNumber);
        mUsersDBref.child(userId).setValue(user).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                hideKeyboard(RegistrationActivity.this);
                Log.d(TAG, "Error: " + task.getException().getLocalizedMessage());
            } else {
                hideKeyboard(RegistrationActivity.this);
                Intent toHomePage = new Intent(RegistrationActivity.this, ParkingLotListActivity.class);
                startActivity(toHomePage);
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
