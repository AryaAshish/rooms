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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
public class SavedHotelFragment extends Fragment {

    private Toolbar toolbar;
    private TextView mSavedHotelTextView2;
    ProgressDialog pd;
    private RecyclerView favouritesRecyclerView;
    private RelativeLayout firstFavouritesLayout,notFirstFavouritesLayout;
    DatabaseReference databaseReference;
    HotelAdapter favouritesAdapter;
    //List<String> mVehicleNo;
    List<String> mVehicleNames;
    List<String> mVehicleLocations;
    List<String> mPricePerHour;
    List<String> mPricePerDay;
    List<String> mVehicleImages;
    List<String> VendorNames;
    List<String> noOfVehicles;
    List<String> vehicleType;
    List<String> mCity;
    List<String> noOfVendors;
    Context favouritesContext;
    List<String> orderId,txnAmount,bankTxnId,txnId,bankName;
    ArrayList<Integer> mFavouriteImages = new ArrayList<Integer>(100);

    public static SavedHotelFragment newInstance() {
        // Required empty public constructor
        SavedHotelFragment fragment = new SavedHotelFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVehicleNames = new ArrayList<String>();
        mVehicleLocations = new ArrayList<String>();
        mPricePerHour = new ArrayList<String>();
        mPricePerDay = new ArrayList<String>();
        mVehicleImages = new ArrayList<String>();
        VendorNames = new ArrayList<String>();
        noOfVehicles = new ArrayList<String>();
        mCity = new ArrayList<String>();
        vehicleType = new ArrayList<String>();
        noOfVendors = new ArrayList<String>();
        favouritesContext = getActivity().getApplicationContext();
        orderId = new ArrayList<String>();
        txnAmount = new ArrayList<String>();
        bankTxnId = new ArrayList<String>();
        txnId = new ArrayList<String>();
        bankName = new ArrayList<String>();
        HotelAdapter.content = "Favourites";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_hotel, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        mSavedHotelTextView2 = (TextView)view.findViewById(R.id.saved_hotel_fragment_txt_2) ;
        mSavedHotelTextView2.setText(Html.fromHtml(getResources().getString(R.string.saved_hotel_txt_2)));
        favouritesRecyclerView = (RecyclerView)view.findViewById(R.id.favouritesRecyclerView);
        firstFavouritesLayout = (RelativeLayout)view.findViewById(R.id.firstFavouritesLayout);
        notFirstFavouritesLayout = (RelativeLayout)view.findViewById(R.id.notFirstFavouritesLayout);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + FirstRunSecondActivity.userUid);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(" ");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //way of fetching details need to be changed

                mPricePerDay.clear();
                mPricePerHour.clear();
                mFavouriteImages.clear();
                mVehicleNames.clear();
                mVehicleImages.clear();
                noOfVehicles.clear();
                noOfVendors.clear();
                mCity.clear();
                vehicleType.clear();
                orderId.clear();
                bankTxnId.clear();
                bankName.clear();
                txnAmount.clear();
                txnId.clear();

                if (dataSnapshot.hasChild("Favourites")){
                    firstFavouritesLayout.setVisibility(View.INVISIBLE);
                    notFirstFavouritesLayout.setVisibility(View.VISIBLE);

                    final DataSnapshot dataSnapshot1 = dataSnapshot.child("Favourites");

                    for (final DataSnapshot snap : dataSnapshot1.getChildren()) {

                        mVehicleNames.add(snap.child("VehicleName").getValue(String.class));
                        mCity.add(snap.child("City").getValue(String.class)); //change mcity later as list of strings
                        mFavouriteImages.add(R.drawable.ic_favorite_red_24dp);

                    }

                    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            for (int i=0;i<mVehicleNames.size();i++){

                                DataSnapshot chidSnap = dataSnapshot2.child(mCity.get(i)).child("room").child(mVehicleNames.get(i));

                                Log.i("ref",chidSnap.getRef().toString());

                                DataSnapshot snapshot1 = chidSnap.child("ParkingAddress");

                                for (DataSnapshot snap1 : snapshot1.getChildren()){

                                    if ("true".equals(snap1.child("isVehicleBlocked").getValue(String.class)) || "BlockedByAdmin".equals(snap1.child("isVehicleBlocked").getValue(String.class)) || "Pending".equals(snap1.child("status").getValue(String.class))){

                                        //dont add the room
                                    }
                                    else {

                                        mVehicleImages.add(chidSnap.child("VehiclePhoto").getValue(String.class));
                                        mPricePerHour.add(chidSnap.child("PricePerHour").getValue(String.class));
                                        mPricePerDay.add(chidSnap.child("PricePerDay").getValue(String.class));
                                        noOfVehicles.add(chidSnap.child("NoOfVehiclesAvailable").getValue(String.class));
                                        vehicleType.add("room");
                                        orderId.add(null);
                                        bankTxnId.add(null);
                                        bankName.add(null);
                                        txnAmount.add(null);
                                        txnId.add(null);
                                        noOfVendors.add(snap1.child("Address").getValue(String.class));

                                    }

                                }

                            }

                            pd.dismiss();

                            favouritesAdapter = new HotelAdapter(favouritesContext,mVehicleImages,mFavouriteImages,mCity,vehicleType,mVehicleNames,mPricePerHour,mPricePerDay,noOfVendors,noOfVehicles,orderId,txnAmount,bankTxnId,txnId,bankName);
                            favouritesRecyclerView.setAdapter(favouritesAdapter);
                            favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(favouritesContext));
                            favouritesRecyclerView.setMotionEventSplittingEnabled(false);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else{

                    pd.dismiss();
                    firstFavouritesLayout.setVisibility(View.VISIBLE);
                    notFirstFavouritesLayout.setVisibility(View.INVISIBLE);
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
                /*getFragmentManager().popBackStack();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, HomeFragment.newInstance(),"CURRENT_FRAGMENT");
                transaction.commit();*/
                View view = FirstActivity.bottomNavigationView.findViewById(R.id.home_menu_item);
                view.performClick();
                return true; //Notice you must returning true here

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

