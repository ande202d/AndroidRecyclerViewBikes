package com.example.recyclerviewbikes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BicyclesService {
    @GET("Bicycles/id/{bikeId}")
    Call<Bike> getBike(@Path("bikeId") int bikeId);

    @GET("Bicycles/")
    Call<ArrayList<Bike>> getAllBikes();

    @GET("Bicycles/")
    Call<List<Bike>> getAllBikesList();
}
