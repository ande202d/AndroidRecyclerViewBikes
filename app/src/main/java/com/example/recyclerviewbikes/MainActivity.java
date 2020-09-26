package com.example.recyclerviewbikes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    ArrayList<Bike> Bikes = new ArrayList<>();
    private static final String LOG_TAG = "MINE";
    private TextView messageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageView = findViewById(R.id.mainTextViewMessage);

        Bikes.add(new Bike(1, "1WE31", "Man", "Kildemoes", "Silver black", "Roskilde", "20200913", 1, "missing"));
        //Bikes.add(new Bike(2, "2WE31", "Woman", "Kildemoes", "Pink", "København", "20200914", 2, "missing"));
        //Bikes.add(new Bike(3, "3WE31", "Woman", "Kildemoes", "Dark purple", "Køge", "20200915", 1, "found"));
        //Bikes.add(new Bike(4, "4WE31", "Man", "Kildemoes", "Lightning red", "Slagelse", "20200916", 3, "found"));
        //Bikes.add(new Bike(5, "5WE31", "Man", "Kildemoes", "Lightning red", "Slagelse", "20200916", 3, "found"));
        //Bikes.add(new Bike(6, "6WE31", "Man", "Kildemoes", "Lightning red", "Slagelse", "20200916", 3, "found"));

        //RecyclerView -stuff
        RecyclerView recyclerView = findViewById(R.id.rvBikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, Bikes);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void getAndShowAllBikes(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-bicyclefinder.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BicyclesService service = retrofit.create(BicyclesService.class);

        Call<List<Bike>> callAllBikes = service.getAllBikesList();

        callAllBikes.enqueue(new Callback<List<Bike>>() {
            @Override
            public void onResponse(Call<List<Bike>> call, Response<List<Bike>> response) {
                if (response.isSuccessful()){
                    Log.d(LOG_TAG, response.message());
                    //Bikes.add(new Bike(69, "69", "69", "69", "69", "69", "69", 69, "found"));
                    Bikes.clear();
                    Bikes.addAll(response.body());
                    messageView.setText("Vi burde få dataen");
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(LOG_TAG, response.message());
                    messageView.setText(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Bike>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
                messageView.setText(t.getMessage());
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        Bike bike = adapter.getItem(position);
        Toast.makeText(this, "You clicked" + bike + " on row: " + position, Toast.LENGTH_LONG).show();
    }

    public void ReloadList(View view) {
        getAndShowAllBikes();
    }

    public void ClearList(View view) {
        if (Bikes.size() > 1){
            Bikes.remove(Bikes.size() - 1);
        } else {
            messageView.setText("Cannot remove when there is only one item left");
        }
        adapter.notifyDataSetChanged();
    }
}