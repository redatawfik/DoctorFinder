package com.doctor.finder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.finder.model.Doctor;
import com.doctor.finder.model.DoctorSearchResponse;
import com.doctor.finder.rest.ApiClient;
import com.doctor.finder.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        demo = findViewById(R.id.demo);
        getData();
    }

    private void getData() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<DoctorSearchResponse> call = apiService.getDoctorList(
                "",
                "",
                "37.773,-122.413,100",
               "37.773,-122.413",
                "male",
                "12cb42313db42ce31f000d55c6acde9c");

        call.enqueue(new Callback<DoctorSearchResponse>() {
            @Override
            public void onResponse(Call<DoctorSearchResponse> call, Response<DoctorSearchResponse> response) {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();

                Doctor doc = response.body().getData().get(0);
                demo.setText(doc.getProfile().getFirst_name());
            }

            @Override
            public void onFailure(Call<DoctorSearchResponse> call, Throwable t) {

                demo.setText(t.getMessage());
                Toast.makeText(MainActivity.this, t + "", Toast.LENGTH_LONG).show();
            }
        });
    }
}
