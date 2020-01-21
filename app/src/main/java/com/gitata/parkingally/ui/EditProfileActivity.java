package com.gitata.parkingally.ui;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gitata.parkingally.R;
import com.gitata.parkingally.models.User;
import com.gitata.parkingally.util.CircleTransformer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_IMAGE_PICK = 2;

    //views
    private ImageView mUserPhotoImageView;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mPhoneNumber;
    private TextView mEmailAddress;
    private TextView mPassword;
    private TextView mEditPhoto;

    //Firebase
    private DatabaseReference mUserDBRef;
    private StorageReference mProfilePhotosStorageRef;
    private String mCurrentUserID;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    private User currentUser;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before);

        //init
        mUserPhotoImageView = findViewById(R.id.imgProfilePhoto);
        mFirstName = findViewById(R.id.fname);
        mLastName = findViewById(R.id.sname);
        mPhoneNumber = findViewById(R.id.pNumber);
        mEmailAddress = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mEditPhoto = findViewById(R.id.edit_photo);

        //onclick handlers
        mUserPhotoImageView.setOnClickListener(this);
        mFirstName.setOnClickListener(this);
        mLastName.setOnClickListener(this);
        mPhoneNumber.setOnClickListener(this);
        mEmailAddress.setOnClickListener(this);
        mPassword.setOnClickListener(this);

        mCurrentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        //init Firebase
        mUserDBRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mProfilePhotosStorageRef = FirebaseStorage.getInstance().getReference().child("ProfilePhotos");

    }

    // TODO: Actualize image capture using a camera
    private void takePictureIntent() {
    }

    private void pickFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mUserPhotoImageView.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //TODO: Actualize image capturing using camera here
//        else if () {
//
//        }
        setProfilePhoto();
    }

    private void setProfilePhoto() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlerDialogStyle);
            progressDialog.setTitle("Uploading");
            progressDialog.setMessage("Just a sec...");
            progressDialog.show();
            StorageReference ref = mProfilePhotosStorageRef.child(mCurrentUserID.toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Picasso.get()
                            .load(currentUser.getProfilePhoto())
                            .resize(mUserPhotoImageView.getMeasuredWidth(), mUserPhotoImageView.getMeasuredHeight())
                            .centerCrop()
                            .transform(new CircleTransformer())
                            .into(mUserPhotoImageView);
                    Log.d("EDIT_ACCT", "Uploaded succesfully");

                    mProfilePhotosStorageRef.child(mCurrentUserID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String userPhotoLink = uri.toString();
                            Map<String, Object> childUpates = new HashMap<>();
                            childUpates.put("profilePhoto", userPhotoLink);
                            mUserDBRef.child(mCurrentUserID).updateChildren(childUpates);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Unable to upload profile photo right now. Please try again later." + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }


    private void updateFirstName(String newFirstName) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("firstName", newFirstName);
        mUserDBRef.child(mCurrentUserID).updateChildren(childUpdates);
        Toast.makeText(EditProfileActivity.this, "Your changes have been updated succesfully", Toast.LENGTH_SHORT).show();
    }

    private void updateLasttName(String newLastName) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("lastName", newLastName);
        mUserDBRef.child(mCurrentUserID).updateChildren(childUpdates);
        Toast.makeText(EditProfileActivity.this, "Your changes have been updated succesfully", Toast.LENGTH_SHORT).show();

    }

    private void updatePhoneNumber(String newPhoneNumber) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("phoneNumber", newPhoneNumber);
        mUserDBRef.child(mCurrentUserID).updateChildren(childUpdates);
        Toast.makeText(EditProfileActivity.this, "Your changes have been updated succesfully", Toast.LENGTH_SHORT).show();

    }

    private void updateEmail(final String newEmail) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential("email", "password");
        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    Log.d("TAG", "User reauthenticated");
                    //change email address
                    user.updateEmail(newEmail)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(EditProfileActivity.this, "Your changes have been updated succesfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                });


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("email", newEmail);
        mUserDBRef.child(mCurrentUserID).updateChildren(childUpdates);
    }

    private void updatePassword(final String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential("email", "password");
        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    Log.d("TAG", "User reauthenticated");
                    //change email address
                    final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                    assert user1 != null;
                    user1.updatePassword(newPassword)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(EditProfileActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Error. Password not updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserDBRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                try {
                    String firstName = currentUser.getFirstName();
                    String lastName = currentUser.getLastName();
                    String phoneNumber = currentUser.getPhoneNumber();
                    phoneNumber = "+254" + "-" + phoneNumber.replaceFirst("^0+(?!$)", "");
                    String email = currentUser.getEmail();
                    mFirstName.setText(firstName);
                    mLastName.setText(lastName);
                    mPhoneNumber.setText(phoneNumber);
                    mEmailAddress.setText(email);
                    mPassword.setText("*******");

                    String image = currentUser.getProfilePhoto();
                    if (!image.equals(currentUser.getProfilePhoto())) {
                        Picasso.get().
                                load(currentUser.getProfilePhoto())
                                .placeholder(R.drawable.ic_account_settings_person);

                    } else {
                        Picasso.get().
                                load(currentUser.getProfilePhoto())
                                .resize(mUserPhotoImageView.getMeasuredWidth(), mUserPhotoImageView.getMeasuredHeight())
                                .transform(new CircleTransformer())
                                .centerCrop().into(mUserPhotoImageView);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgProfilePhoto:
                pickFromGallery();
                break;

            case R.id.fname:
                builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("First Name");
                final EditText input_fname = new EditText(this);
                String firstName = currentUser.getFirstName();
                input_fname.setText(firstName);
                input_fname.setSelection(input_fname.getText().length());
                input_fname.setWidth(500);
                builder.setView(input_fname);

                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog = builder.create();
                alertDialog.setOnShowListener(dialog -> {
                    Button btnPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPositive.setOnClickListener(v12 -> {
                        String userFirstName = input_fname.getText().toString().trim();
                        if (userFirstName.isEmpty()) {
                            input_fname.setError("First name expected");
                            input_fname.requestFocus();
                        } else {
                            userFirstName = input_fname.getText().toString().trim();
                            try {
                                updateFirstName(userFirstName);
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                });
                alertDialog.show();
                break;

            case R.id.sname:
                builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Last Name");
                final EditText input_lname = new EditText(this);
                String lastName = currentUser.getLastName();
                input_lname.setText(lastName);
                input_lname.setSelection(input_lname.getText().length());
                input_lname.setWidth(500);
                builder.setView(input_lname);

                builder.setPositiveButton("SAVE", (dialog, which) -> {

                });
                alertDialog = builder.create();
                alertDialog.setOnShowListener(dialog -> {
                    Button btnPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPositive.setOnClickListener(v13 -> {
                        String userLastName = input_lname.getText().toString().trim();
                        if (userLastName.isEmpty()) {
                            input_lname.setError("Last name expected");
                            input_lname.requestFocus();
                        } else {
                            userLastName = input_lname.getText().toString().trim();
                            try {
                                updateLasttName(userLastName);
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                });
                alertDialog.show();
                break;
            case R.id.pNumber:
                builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Phone Number");
                final EditText input_phoneNumber = new EditText(this);
                String phoneNumber = currentUser.getPhoneNumber();
                input_phoneNumber.setText(phoneNumber);
                input_phoneNumber.setSelection(input_phoneNumber.getText().length());
                input_phoneNumber.setWidth(500);
                builder.setView(input_phoneNumber);

                builder.setPositiveButton("SAVE", (dialog, which) -> {

                });
                alertDialog = builder.create();
                alertDialog.setOnShowListener(dialog -> {
                    Button btnPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPositive.setOnClickListener(v14 -> {
                        String userPhoneNumber = input_phoneNumber.getText().toString().trim();
                        if (userPhoneNumber.isEmpty()) {
                            input_phoneNumber.setError("A phone number is expected");
                            input_phoneNumber.requestFocus();
                        } else if (userPhoneNumber.length() != 10) {
                            input_phoneNumber.setError("Phone number is not valid");
                            input_phoneNumber.requestFocus();
                        } else {
                            userPhoneNumber = input_phoneNumber.getText().toString().trim();
                            try {
                                updatePhoneNumber(userPhoneNumber);
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                });
                alertDialog.show();
                break;
            case R.id.email:
                builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Email");
                final EditText input_email = new EditText(this);
                input_email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                String email = currentUser.getEmail();
                input_email.setText(email);
                input_email.setSelection(input_email.getText().length());
                input_email.setWidth(500);
                builder.setView(input_email);

                builder.setPositiveButton("SAVE", (dialog, which) -> {

                });
                alertDialog = builder.create();
                alertDialog.setOnShowListener(dialog -> {
                    Button btnPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPositive.setOnClickListener(v15 -> {
                        String userEmail = input_email.getText().toString().trim();
                        if (userEmail.isEmpty()) {
                            input_email.setError("Email address expected");
                            input_email.requestFocus();
                            return;
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            input_email.setError("Email address is not valid");
                            input_email.requestFocus();
                            return;
                        } else
                            userEmail = input_email.getText().toString().trim();
                        try {
                            updateEmail(userEmail);
                            alertDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
                alertDialog.show();
                break;
            case R.id.password:
                builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Password");
                final EditText input_password = new EditText(this);
                input_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input_password.setHint("Enter a new password");
                input_password.setSelection(input_password.getText().length());
                input_password.setWidth(500);
                builder.setView(input_password);

                builder.setPositiveButton("Update Password", (dialog, which) -> {

                });
                alertDialog = builder.create();
                alertDialog.setOnShowListener(dialog -> {
                    Button btnPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPositive.setOnClickListener(v1 -> {
                        String userPassword = input_password.getText().toString().trim();
                        if (userPassword.length() < 6) {
                            input_password.setError("Secure passwords are at least 6 characters long");
                            input_password.requestFocus();
                        } else {
                            userPassword = input_password.getText().toString().trim();
                            try {
                                updatePassword(userPassword);
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                });
                alertDialog.show();
                break;
        }
    }
}

