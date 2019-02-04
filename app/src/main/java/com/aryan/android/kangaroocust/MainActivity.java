package com.aryan.android.kangaroocust;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView mOyo;
    static boolean isSearchActivity;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOyo = (ImageView) findViewById(R.id.oyo_main) ;

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //  Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/anagram.ttf");
        //mOyo.setTypeface(custom_font);
        //mOyo.setLetterSpacing(-0.03f);

        if (isConnected) {

            //internet connection is there

            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();

            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   /* for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        if (snapshot.getKey().equals("Admins") || snapshot.getKey().equals("Bookings") || snapshot.getKey().equals("Coupons") || snapshot.getKey().equals("Users") || snapshot.getKey().equals("Vendors")){

                            //do nothing

                        }
                        else {

                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                for (DataSnapshot snapshot2 : snapshot1.getChildren()){

                                    DataSnapshot dataSnapshot1 = snapshot2.child("ParkingAddress");

                                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){


                                        if (dataSnapshot2.child("AvailableToDate").getValue(String.class).equals("-NA-")){

                                            //do nothing

                                        }
                                        else {

                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                                            String availableToDate = dataSnapshot2.child("AvailableToDate").getValue(String.class);

                                            try {

                                                Calendar c = Calendar.getInstance();

                                                int year = c.get(Calendar.YEAR);
                                                int month = c.get(Calendar.MONTH) + 1;
                                                int day = c.get(Calendar.DAY_OF_MONTH);

                                                String date = day + "-" + month + "-" + year;

                                                Date validTill = simpleDateFormat.parse(availableToDate);
                                                Date todayDate = simpleDateFormat.parse(date);

                                                if (todayDate.after(validTill)){

                                                    Log.i("status","entered");

                                                    String vendorUid = dataSnapshot2.child("VendorUid").getValue(String.class);
                                                    final String name = snapshot2.getKey();
                                                    final String type = snapshot1.getKey();
                                                    final String city = snapshot.getKey();
                                                    final String parkingAddress = dataSnapshot2.child("Address").getValue(String.class);

                                                    int noOfVehiclesToRemove = Integer.parseInt(dataSnapshot2.child("NoOfVehicles").getValue(String.class));

                                                    int totalVehicles = Integer.parseInt(snapshot2.child("NoOfVehiclesAvailable").getValue(String.class));

                                                    if (totalVehicles - noOfVehiclesToRemove <= 0){

                                                        snapshot2.getRef().removeValue();

                                                    }
                                                    else {

                                                        snapshot2.child("NoOfVehiclesAvailable").getRef().setValue(Integer.toString(totalVehicles - noOfVehiclesToRemove));

                                                        dataSnapshot2.getRef().removeValue();

                                                    }

                                                    //not being deleted in vendors uploaded vehicles?

                                                    DatabaseReference vendorReference = FirebaseDatabase.getInstance().getReference("Vendors/" + vendorUid + "/UploadedVehicles");

                                                    vendorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            for (DataSnapshot snap : dataSnapshot.getChildren()){

                                                                if (snap.child("VehicleName").getValue(String.class).equals(name) && snap.child("VehicleType").getValue(String.class).equals(type) && snap.child("City").getValue(String.class).equals(city) && snap.child("ParkingAddress").getValue(String.class).equals(parkingAddress)){

                                                                    Log.i("s","entered");
                                                                    snap.getRef().removeValue();

                                                                }

                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }

                                            }
                                            catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }*/

                    goToFirstActivity();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {

            //internet connection is not there
            Toast.makeText(this, "No internet connection.Please check your network and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToFirstActivity(){

        Log.d("MainActivity","yes");

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

       // HotelActivity_1.pickUpString = "-NA-";
       // HotelActivity_1.deliveryString = "-NA-";

        if (isFirstRun) {

            //the app is run for the first time
            //go to login or signup page

            new Timer().schedule(new TimerTask() {
                public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isFirstRun", false)
                                    .apply();

                            Intent intent = new Intent(MainActivity.this,FirstRunSecondActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
                }
            }, 3000);

        } else {

            //the app is not run for the first time
            //check if there is a saved auth state

            DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference();

            statusReference.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //incase the admin deletes the user at a time when the user is currently logged in,then the user is instantly logged out

                    if (FirstRunSecondActivity.userUid != null){

                        if (!dataSnapshot.hasChild(FirstRunSecondActivity.userUid)){

                            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                                FirebaseAuth.getInstance().signOut();
                            }

                            getSharedPreferences("User",MODE_PRIVATE)
                                    .edit()
                                    .putString("UserUid",null)
                                    .apply();

                            getSharedPreferences("UserName",MODE_PRIVATE)
                                    .edit()
                                    .putString("Name",null)
                                    .apply();

                            Toast.makeText(MainActivity.this, "You were logged out.Please login again", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this,FirstRunSecondActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            String isUserUid = getSharedPreferences("User",MODE_PRIVATE)
                    .getString("UserUid",null);

            String UserName = getSharedPreferences("UserName",MODE_PRIVATE)
                    .getString("Name",null);

            FirstRunSecondActivity.userName = UserName;

            FirstRunSecondActivity.userUid = isUserUid;


            if (isUserUid == null) {

                //no saved auth state
                //show start activity

                new Timer().schedule(new TimerTask() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                Intent intent = new Intent(MainActivity.this,FirstRunSecondActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        });
                    }
                }, 3000);

            } else {

                //there is a saved auth state

                //check if the saved auth state is currently active or deleted by the admin



                statusReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("Users").hasChild(FirstRunSecondActivity.userUid)){

                            //user not deleted by admin

                            new Timer().schedule(new TimerTask() {
                                public void run() {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {

                                            Intent intent = new Intent(MainActivity.this,FirstActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                        }
                                    });
                                }
                            }, 3000);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        }

    }
}
