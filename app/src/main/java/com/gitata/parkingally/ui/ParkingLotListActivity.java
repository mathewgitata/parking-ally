package com.gitata.parkingally.ui;

import android.os.Bundle;

import com.gitata.parkingally.R;
import com.gitata.parkingally.adapters.ParkingLotListAdapter;
import com.gitata.parkingally.models.EmelParkingLotResponse;
import com.gitata.parkingally.models.NavBarItem;
import com.gitata.parkingally.network.EmelParkingApi;
import com.gitata.parkingally.network.EmelParkingClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
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

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ParkingLotListAdapter mAdapter;
    public ArrayList<EmelParkingLotResponse> mParkingLotsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_list);
        ButterKnife.bind(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

    private void setNavBarButtons() {
        for (NavBarItem item : NavBarItem.values()) {
            TextView itemView = findViewById(item.getItemId());
            itemView.setOnClickListener(this);
        }
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
        //TODO: Redirect user to account settings page
    }
}
