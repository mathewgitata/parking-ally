package com.gitata.parkingally;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EmelParkingApi {
    @GET("parking/lots")
    Call<List<EmelParkingSearchResponse>> getParkingLots();
}
