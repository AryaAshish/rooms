package com.aryan.android.kangaroocust;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayImages extends AppCompatActivity {

    List<String> vehicleImageViews;
    DatabaseReference imageReference;
    ImageAdapter imageAdapter;
    private TextView mToolText;
    private Toolbar toolbar;
    RecyclerView imagesRecyclerView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            overridePendingTransition(R.anim.activit_back_in, R.anim.activity_back_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);

        vehicleImageViews = new ArrayList<String>();
        mToolText = (TextView) findViewById(R.id.text);
        mToolText.setText("Extra Images");
        toolbar = (Toolbar) findViewById(R.id.activity_images_toolbar);
        imagesRecyclerView = (RecyclerView)findViewById(R.id.images_recycler_view);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imageReference = FirebaseDatabase.getInstance().getReference(HotelAdapter.city + "/" + HotelAdapter.type + "/" + HotelAdapter.name).child("ExtraImages");

        imageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vehicleImageViews.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        vehicleImageViews.add(snapshot1.getValue(String.class));
                    }

                }

                imageAdapter = new ImageAdapter(DisplayImages.this,vehicleImageViews);
                imagesRecyclerView.setAdapter(imageAdapter);
                imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                imagesRecyclerView.setMotionEventSplittingEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

