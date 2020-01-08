package com.gitata.parkingally.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.gitata.parkingally.EmelParkingApi;
import com.gitata.parkingally.EmelParkingClient;
import com.gitata.parkingally.EmelParkingSearchResponse;
import com.gitata.parkingally.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomePageActivity extends AppCompatActivity {
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        textViewResult = findViewById(R.id.text_view_result);

        EmelParkingApi client = EmelParkingClient.getClient();

        Call<List<EmelParkingSearchResponse>> call = client.getParkingLots();
        call.enqueue(new Callback<List<EmelParkingSearchResponse>>() {
            @Override
            public void onResponse(Call<List<EmelParkingSearchResponse>> call, Response<List<EmelParkingSearchResponse>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<EmelParkingSearchResponse> parkingLots = response.body();
                for (EmelParkingSearchResponse parkingLot : parkingLots) {
                    String content = "";
                    content += "Capacity: " + parkingLot.getMaxCapacity();
                    textViewResult.setText(content);
                }
            }

            @Override
            public void onFailure(Call<List<EmelParkingSearchResponse>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
