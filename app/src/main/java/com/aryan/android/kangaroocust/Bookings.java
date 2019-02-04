package com.aryan.android.kangaroocust;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Bookings extends Fragment {

    private Toolbar toolbar;
    private RelativeLayout firstBookingLayout;
    private RelativeLayout notFirstBookingLayout;
    private RecyclerView bookingsRecyclerView;
    List<String> mVehicleNames;
    List<String> mPricePerHour;
    List<String> mPricePerDay;
    List<String> mVehicleImages;
    List<String> noOfVehicles;
    List<String> noOfVendors;
    static List<String> pickUpStrings;
    static List<String> deliveryStrings;
    ArrayList<Integer> mFavouriteImages = new ArrayList<Integer>(100);
    HotelAdapter bookingsAdapter;
    List<String> vehicleType;
    List<String> mCity;
    String isAddedToFavourites;
    DatabaseReference databaseReference;
    List<String> orderId,txnAmount,bankTxnId,txnId,bankName;
    Context bookingsContext;
    ProgressDialog pd;

    public static Bookings newInstance() {
        // Required empty public constructor
        Bookings fragment = new Bookings();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVehicleNames = new ArrayList<String>();
        noOfVendors = new ArrayList<String>();
        mPricePerHour = new ArrayList<String>();
        mPricePerDay = new ArrayList<String>();
        mVehicleImages = new ArrayList<String>();
        noOfVehicles = new ArrayList<String>();
        mCity = new ArrayList<String>();
        vehicleType = new ArrayList<String>();
        orderId = new ArrayList<String>();
        txnAmount = new ArrayList<String>();
        bankTxnId = new ArrayList<String>();
        txnId = new ArrayList<String>();
        bankName = new ArrayList<String>();
        pickUpStrings = new ArrayList<String>();
        deliveryStrings = new ArrayList<String>();
        bookingsContext = getActivity().getApplicationContext();
        isAddedToFavourites = "false";
        HotelAdapter.content = "Bookings";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.my_bookings_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).setTitle(" ");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        firstBookingLayout = (RelativeLayout)view.findViewById(R.id.firstBookingLayout);
        notFirstBookingLayout = (RelativeLayout)view.findViewById(R.id.notFirstBookingLayout);
        bookingsRecyclerView = (RecyclerView) view.findViewById(R.id.bookingsRecyclerView);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + FirstRunSecondActivity.userUid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mPricePerDay.clear();
                mPricePerHour.clear();
                noOfVendors.clear();
                mVehicleNames.clear();
                mVehicleImages.clear();
                noOfVehicles.clear();
                mCity.clear();
                vehicleType.clear();
                mFavouriteImages.clear();
                orderId.clear();
                bankTxnId.clear();
                bankName.clear();
                txnAmount.clear();
                txnId.clear();
                pickUpStrings.clear();
                deliveryStrings.clear();

                if (dataSnapshot.hasChild("Bookings")){
                    firstBookingLayout.setVisibility(View.INVISIBLE);
                    notFirstBookingLayout.setVisibility(View.VISIBLE);

                    DataSnapshot snapshot = dataSnapshot.child("Bookings");

                    for (DataSnapshot snap : snapshot.getChildren()) {

                        mVehicleImages.add(snap.child("VehiclePhoto").getValue(String.class));
                        mVehicleNames.add(snap.child("VehicleName").getValue(String.class));
                        mPricePerHour.add(snap.child("PricePerHour").getValue(String.class));
                        mPricePerDay.add(snap.child("PricePerDay").getValue(String.class));
                        mFavouriteImages.add(R.drawable.ic_favorite_black_24dp);
                        noOfVehicles.add(snap.child("BookedNoOfVehicles").getValue(String.class) + " Room Booked");
                        noOfVendors.add("Booked from " + snap.child("Vendor").getValue(String.class));
                        mCity.add(snap.child("City").getValue(String.class)); //change mcity later as list of strings
                        vehicleType.add(snap.child("VehicleType").getValue(String.class));
                        orderId.add(snap.child("OrderId").getValue(String.class));
                        bankTxnId.add(snap.child("BankTxnId").getValue(String.class));
                        bankName.add(snap.child("BankName").getValue(String.class));
                        txnAmount.add(snap.child("TxnAmount").getValue(String.class));
                        txnId.add(snap.child("TxnId").getValue(String.class));
                        pickUpStrings.add(snap.child("PickUp").getValue(String.class));
                        deliveryStrings.add(snap.child("Delivery").getValue(String.class));

                    }

                    pd.dismiss();

                    bookingsAdapter = new HotelAdapter(bookingsContext,mVehicleImages,mFavouriteImages,mCity,vehicleType,mVehicleNames,mPricePerHour,mPricePerDay,noOfVendors,noOfVehicles,orderId,txnAmount,bankTxnId,txnId,bankName);
                    bookingsRecyclerView.setAdapter(bookingsAdapter);
                    bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(bookingsContext));
                    bookingsRecyclerView.setMotionEventSplittingEnabled(false);
                }
                else {

                    pd.dismiss();
                    firstBookingLayout.setVisibility(View.VISIBLE);
                    notFirstBookingLayout.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

            }
        });

        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                View view = FirstActivity.bottomNavigationView.findViewById(R.id.home_menu_item);
                view.performClick();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


