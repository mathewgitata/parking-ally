package com.gitata.parkingally.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gitata.parkingally.R;
import com.gitata.parkingally.adapters.ParkingLotListAdapter;
import com.gitata.parkingally.models.EmelParkingLotResponse;
import com.gitata.parkingally.network.EmelParkingApi;
import com.gitata.parkingally.network.EmelParkingClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotListActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ParkingLotListAdapter mAdapter;
    public ArrayList<EmelParkingLotResponse> mParkingLotsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_list);
        ButterKnife.bind(this);

        EmelParkingApi client = EmelParkingClient.getClient();
        Call<List<EmelParkingLotResponse>> call = client.getParkingLots();

        call.enqueue(new Callback<List<EmelParkingLotResponse>>() {
            @Override
            public void onResponse(Call<List<EmelParkingLotResponse>> call, Response<List<EmelParkingLotResponse>> response) {
                if (response.isSuccessful()) {
                    mParkingLotsList.addAll(response.body());
                    mAdapter = new ParkingLotListAdapter(ParkingLotListActivity.this, mParkingLotsList);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(ParkingLotListActivity.this);
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


}
