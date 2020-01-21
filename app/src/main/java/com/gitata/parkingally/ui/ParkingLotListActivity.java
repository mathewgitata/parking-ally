package com.gitata.parkingally.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.gitata.parkingally.R;
import com.gitata.parkingally.adapters.ParkingLotListAdapter;
import com.gitata.parkingally.models.EmelParkingLotResponse;
import com.gitata.parkingally.models.NavBarItem;
import com.gitata.parkingally.models.User;
import com.gitata.parkingally.network.EmelParkingApi;
import com.gitata.parkingally.network.EmelParkingClient;
import com.gitata.parkingally.util.CircleTransformer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotListActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDBRef;
    private User currentUser;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ParkingLotListAdapter mAdapter;
    public ArrayList<EmelParkingLotResponse> mParkingLotsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_list);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUsersDBRef = FirebaseDatabase.getInstance().getReference().child("Users");

        inflateViews();

        EmelParkingApi client = EmelParkingClient.getClient();
        Call<List<EmelParkingLotResponse>> call = client.getParkingLots();

        call.enqueue(new Callback<List<EmelParkingLotResponse>>() {
            @Override
            public void onResponse(Call<List<EmelParkingLotResponse>> call, Response<List<EmelParkingLotResponse>> response) {
                if (response.isSuccessful()) {
                    mParkingLotsList.addAll(response.body());
                    mAdapter = new ParkingLotListAdapter(com.gitata.parkingally.ui.ParkingLotListActivity.this, mParkingLotsList);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(com.gitata.parkingally.ui.ParkingLotListActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);

                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<EmelParkingLotResponse>> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mUsersDBRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    try {
                        NavigationView navigationView = findViewById(R.id.nav_view);
                        View headerView = navigationView.getHeaderView(0);
                        ImageView mUserPhotoImageView = headerView.findViewById(R.id.img_profile_photo);
                        TextView mUserName = headerView.findViewById(R.id.tv_user_name);
                        TextView mEmailAddress = headerView.findViewById(R.id.tv_email);

                        String firstName = currentUser.getFirstName();
                        String lastName = currentUser.getLastName();
                        String name = firstName + " " + lastName;
                        String email = currentUser.getEmail();
                        String image = currentUser.getProfilePhoto();
                        mUserName.setText(name);
                        mEmailAddress.setText(email);

                        if (!image.equals(currentUser.getProfilePhoto())) {
                            Picasso.get().load(currentUser.getProfilePhoto()).placeholder(R.drawable.ic_nav_header_account);
                        } else {
                            Picasso.get().load(currentUser.getProfilePhoto()).transform(new CircleTransformer()).resize(mUserPhotoImageView.getMeasuredWidth(), mUserPhotoImageView.getMeasuredHeight()).centerCrop().into(mUserPhotoImageView);
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
    }


    private void inflateViews() {
        drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        setNavBarButtons();
    }


    private void setNavBarButtons() {
        for (NavBarItem item : NavBarItem.values()) {
            TextView itemView = findViewById(item.getItemId());
            itemView.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(Gravity.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        drawer = findViewById(R.id.drawer_layout);
        switch (NavBarItem.fromViewId(v.getId())) {
            case MY_ACCOUNT:
                drawer.closeDrawer(GravityCompat.START);
                toAccountSettings();
                break;
            case PARKING_HISTORY:
                drawer.closeDrawer(GravityCompat.START);
                toParkingHistory();
                break;
            case PAYMENT:
                drawer.closeDrawer(GravityCompat.START);
                toPaymentInfo();
                break;
            case HELP:
                drawer.closeDrawer(GravityCompat.START);
                toFeedback();
                break;
            case LEGAL:
                drawer.closeDrawer(GravityCompat.START);
                toLegalDetails();
                break;
            default:
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void toLegalDetails() {
        //TODO: Redirect user to legal page
    }

    private void toFeedback() {
        //TODO: Redirect user to feedback page
    }

    private void toPaymentInfo() {
        //TODO: Redirect user to payment page
    }

    private void toParkingHistory() {
        //TODO: Redirect user to parking history
    }

    private void toAccountSettings() {
        Intent toAccountSettings = new Intent(ParkingLotListActivity.this, AccountSettingsActivity.class);
        startActivity(toAccountSettings);
    }
}
