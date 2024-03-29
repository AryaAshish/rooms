package com.aryan.android.kangaroocust;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;


public class FirstRunSecondActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button mOneTapLogin;
    RecyclerView spinner_recyclerView;
    List<String> countryNames;
    List<String> code;
    ProgressDialog pd;
    static TypedArray imageArray;
    public static Dialog dialog;
    RecyclerView recyclerView;
    SpinnerRecyclerViewAdapter adapter;
    public static TextView mCountryCode;
    public EditText mPhoneNumber;
    public static ImageView mFlagImage;
    LinearLayout mUserInput;
    RelativeLayout mLayout;
    public String PhoneNumber;
    private FirebaseAuth fbAuth;
    String message;
    String fullNumber;
    String registeredNumber;
    boolean isUserRegistered;
    protected String phoneVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    DatabaseReference mDataBaseReference;
    public static final String EXTRA_MESSAGE =
            "com.aryan.android.kangaroocust.extra.MESSAGE";
    int ref;
    DatabaseReference reference;
    public static String customerName;
    boolean isPhoneNumberVerified,verificationByCode,instantVerification;
    PhoneAuthCredential userCredential;
    static String userUid;
    static String userName;

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
        setContentView(R.layout.activity_first_run_second);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mOneTapLogin = (Button) findViewById(R.id.btn_one_tap_login);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mCountryCode = (TextView) findViewById(R.id.country_code);
        mFlagImage = (ImageView) findViewById(R.id.flag_img);
        mUserInput = (LinearLayout)findViewById(R.id.user_input_in_frsa) ;
        mPhoneNumber = (EditText) findViewById(R.id.editText_phone) ;
        registeredNumber = null;
        fbAuth = FirebaseAuth.getInstance();
        mDataBaseReference = FirebaseDatabase.getInstance().getReference("Users");
        reference = FirebaseDatabase.getInstance().getReference("Users/");

        countryNames = Arrays.asList(getResources().getStringArray(R.array.countries_list));
        code = Arrays.asList(getResources().getStringArray(R.array.countries_code_list));
        imageArray= getResources().obtainTypedArray(R.array.random_imgs);
        isPhoneNumberVerified = false;
        verificationByCode = true;
        instantVerification = false;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_recycler_view);
        dialog.setCanceledOnTouchOutside(true);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view_spinner);
        adapter = new SpinnerRecyclerViewAdapter(this, countryNames, code);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void getDialog(View view) {
        dialog.show();
    }

    public void changeBackground(View view) {
        mUserInput.setBackgroundResource(R.drawable.linear_activity_in_activity_first_run_second_background_on_focus);
    }


    public void verify(View view) {

        InputMethodManager mImMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mImMan.hideSoftInputFromWindow(mPhoneNumber.getWindowToken(), 0);

        PhoneNumber = mPhoneNumber.getText().toString();

        message = mCountryCode.getText() + " " + mPhoneNumber.getText();
        fullNumber = mCountryCode.getText().toString() + mPhoneNumber.getText().toString();

        int i=0;


        if(PhoneNumber.length() == 0){
            mUserInput.setBackground(getResources().getDrawable(R.drawable.error_phone_number));
            Snackbar snackbar= Snackbar.make(mLayout,"Enter phone number",Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.errorSnackBarColor));
            TextView mSnackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            mSnackBarTextView.setTextAlignment(snackBarView.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
        else if(PhoneNumber.length() != 10) {
            mUserInput.setBackground(getResources().getDrawable(R.drawable.error_phone_number));
            Snackbar snackbar= Snackbar.make(mLayout,"Enter valid Phone Number",Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.errorSnackBarColor));
            TextView mSnackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            mSnackBarTextView.setTextAlignment(snackBarView.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
        else {

            SignIn();
        }

    }
    public void SignIn() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ref = 0;
                isUserRegistered = false;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    String data = ds.getValue().toString();

                    Log.i("data", data);

                    DataSnapshot dataSnap = ds.child("PhoneNumber");

                    for (DataSnapshot dsp : dataSnap.getChildren()) {

                        String phoneNum = dsp.getValue().toString();

                        if (fullNumber.equals(phoneNum)) {

                            isUserRegistered = true;
                            break;

                        }
                    }

                }
                if(isUserRegistered){


                    Toast.makeText(getApplicationContext(), "user is already registered", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(getApplicationContext(), FirstRunThirdActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(EXTRA_MESSAGE, message);
                    intent.putExtra("number",PhoneNumber);
                    intent.putExtra("countryCodeMobNumber",fullNumber);
                    intent.putExtra("verificationIdSent",phoneVerificationId);
                    intent.putExtra("reSendToken",resendingToken);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(FirstRunSecondActivity.this, "database error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void loginButton(View v){

        InputMethodManager mImMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mImMan.hideSoftInputFromWindow(mPhoneNumber.getWindowToken(), 0);

        PhoneNumber = mPhoneNumber.getText().toString();

        message = mCountryCode.getText() + " " + mPhoneNumber.getText();
        fullNumber = mCountryCode.getText().toString() + mPhoneNumber.getText().toString();
        int i=0;

        if(PhoneNumber.length() == 0){
            mUserInput.setBackground(getResources().getDrawable(R.drawable.error_phone_number));
            Snackbar snackbar= Snackbar.make(mLayout,"Enter phone number",Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.errorSnackBarColor));
            TextView mSnackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            mSnackBarTextView.setTextAlignment(snackBarView.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
        else if(PhoneNumber.length() != 10) {
            mUserInput.setBackground(getResources().getDrawable(R.drawable.error_phone_number));
            Snackbar snackbar= Snackbar.make(mLayout,"Enter valid Phone Number",Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.errorSnackBarColor));
            TextView mSnackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            mSnackBarTextView.setTextAlignment(snackBarView.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
        else{

            pd = new ProgressDialog(FirstRunSecondActivity.this);
            pd.setMessage("Logging in...");
            pd.show();
            LogIn();
        }

    }

    public void LogIn() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ref = 0;
                isUserRegistered = false;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    String data = ds.getValue().toString();

                    Log.i("data", data);

                    DataSnapshot dataSnap = ds.child("PhoneNumber");

                    for (DataSnapshot dsp : dataSnap.getChildren()) {

                        String phoneNum = dsp.getValue().toString();

                        if (fullNumber.equals(phoneNum)) {   //but returning false

                            isUserRegistered = true;
                            DataSnapshot snapshot = ds.child("Name");
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                customerName = snapshot1.getValue(String.class);
                                userName = snapshot1.getValue(String.class);
                                break;

                            }

                        }
                    }


                }

                if (isUserRegistered){

                    Intent intent = new Intent(getApplicationContext(), VerifyPhone.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(EXTRA_MESSAGE, message);
                    intent.putExtra("number",PhoneNumber);
                    intent.putExtra("countryCodeMobNumber",fullNumber);
                    intent.putExtra("verificationIdSent",phoneVerificationId);
                    intent.putExtra("reSendToken",resendingToken);
                    startActivity(intent);

                }
                else {

                    pd.dismiss();

                    Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

                Toast.makeText(FirstRunSecondActivity.this, "database error", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
