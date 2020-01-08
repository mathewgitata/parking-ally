package com.gitata.parkingally.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.gitata.parkingally.R;
import com.gitata.parkingally.models.EmelParkingLotResponse;
import com.gitata.parkingally.network.EmelParkingApi;
import com.gitata.parkingally.network.EmelParkingClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {
    public ArrayList<EmelParkingLotResponse> mParkingLotsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        EmelParkingApi client = EmelParkingClient.getClient();
        Call<List<EmelParkingLotResponse>> call = client.getParkingLots();

        call.enqueue(new Callback<List<EmelParkingLotResponse>>() {
            @Override
            public void onResponse(Call<List<EmelParkingLotResponse>> call, Response<List<EmelParkingLotResponse>> response) {
                if (response.isSuccessful()) {
                    mParkingLotsList.addAll(response.body());
                    
                }
            }

            @Override
            public void onFailure(Call<List<EmelParkingLotResponse>> call, Throwable t) {

            }
        });

    }
}
