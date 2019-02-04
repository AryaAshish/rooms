package com.aryan.android.kangaroocust;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.slideup.SlideUp;

import java.util.List;

public class HotelActivity extends AppCompatActivity {

    RecyclerView mHotelImagesRecyclerView;
    HotelImageAdapter mAdapter;
    Toolbar toolbar;
    FloatingActionButton fab;
    private SlideUp slideUp;
    private View dim;
    private View slideView;
    List<String> mHotelImages;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        toolbar = (Toolbar) findViewById(R.id.hotel_tool_bar);
        setSupportActionBar(toolbar);

        /*int[] mHotelImages= {
                R.drawable.room,
                R.drawable.room1,
                R.drawable.room2,
                R.drawable.room3,
                R.drawable.room4,
                R.drawable.room5,
                R.drawable.room6,
                R.drawable.room7,
                R.drawable.room8 };*/
        mDatabaseReference = mFirebaseDatabase.getReference("Agartala" + "/Bikes");//to be changed

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mHotelImages.clear();

                //code not working when the variables are being assigned here.when the commenting is taken out,it is returning to the previous activity

                for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {
                    Log.v("tmz",""+ chidSnap.child("HotelLocation").getValue());//gives the value for given keyname
                    mHotelImages.add(chidSnap.child("VehicleImage").getValue(String.class));

                }

                Log.i("status","loop left");

                mHotelImagesRecyclerView = (RecyclerView) findViewById(R.id.hotel_recycler_view);
                mAdapter = new HotelImageAdapter(getApplicationContext(), mHotelImages);
                mHotelImagesRecyclerView.setAdapter(mAdapter);
                mHotelImagesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                slideView = findViewById(R.id.slideView);
                dim = findViewById(R.id.dim);

                slideUp = new SlideUp(slideView);
                slideUp.hideImmediately();

                fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slideUp.animateIn();
                        fab.hide();
                    }
                });

                slideUp.setSlideListener(new SlideUp.SlideListener() {

                    @Override
                    public void onSlideDown(float v) {
                        dim.setAlpha(1 - (v/100));
                    }

                    @Override
                    public void onVisibilityChanged(int i) {
                        if(i == View.GONE) {
                            fab.show();
                        }
                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(HotelActivity.this, "error loading data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
