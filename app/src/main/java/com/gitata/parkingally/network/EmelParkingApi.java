package com.gitata.parkingally.network;

import com.gitata.parkingally.models.EmelParkingLotResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface EmelParkingApi {
    @Headers("api_key: c432d6a3ba4ea59160ffebf70fbb6873")
    @GET("parking/lots")
    Call<List<EmelParkingLotResponse>> getParkingLots();
}
