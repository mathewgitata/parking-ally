package com.gitata.parkingally.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitata.parkingally.R;
import com.gitata.parkingally.models.User;
import com.gitata.parkingally.util.CircleTransformer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSettingsActivity extends AppCompatActivity {

    @BindView(R.id.img_profile_photo)
    ImageView mProfilePhoto;

    @BindView(R.id.tv_user_name)
    TextView mUserName;

    @BindView(R.id.tv_phone_number)
    TextView mPhoneNumber;

    @BindView(R.id.tv_email)
    TextView mEmail;

    @OnClick(R.id.tv_sign_out)
    public void signOutUser() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        AccountSettingsActivity.this.finish();

    }

    //Firebase
    private DatabaseReference mUserDBRef;
    private StorageReference mStorageRef;
    private String mCurrentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ButterKnife.bind(this);

        inflateViews();

        mCurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //init Firebase
        mUserDBRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Photos").child("Users");

        //Populate the views initially
        populateTheViews();
    }

    private void inflateViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before);
    }

    private void populateTheViews() {
        mUserDBRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                try {
                    assert currentUser != null;
                    String userPhoto = currentUser.getImage();
                    String userName = currentUser.getFirstName() + " " + currentUser.getLastName();
                    String phoneNumber = currentUser.getPhoneNumber();
                    phoneNumber = "+254" + "-" + phoneNumber.replaceFirst("^0+(?!$)", "");
                    String email = currentUser.getEmail();

                    Picasso.get().load(userPhoto).into(mProfilePhoto);
                    mUserName.setText(userName);
                    mPhoneNumber.setText(phoneNumber);
                    mEmail.setText(email);

                    String image = currentUser.getProfilePhoto();
                    if (!image.equals(currentUser.getProfilePhoto())) {
                        Picasso.get().load(currentUser.getProfilePhoto()).placeholder(R.drawable.ic_account_settings_person);

                    } else {
                        Picasso.get().load(currentUser.getProfilePhoto()).transform(new CircleTransformer()).resize(mProfilePhoto.getMeasuredWidth(), mProfilePhoto.getMeasuredHeight()).centerCrop().into(mProfilePhoto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void toEditProfile(View view) {
        Intent toEditProfile = new Intent(AccountSettingsActivity.this, EditProfileActivity.class);
        startActivity(toEditProfile);
    }
}
