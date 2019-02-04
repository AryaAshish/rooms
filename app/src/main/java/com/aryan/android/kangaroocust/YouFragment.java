package com.aryan.android.kangaroocust;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class YouFragment extends Fragment {


    private TextView mTitleTextView;
    FirebaseAuth fbAuth;
    public TextView mLogOut;
    public TextView chatWithVendor;
    public TextView profile,wallets,inviteAndEarn,faq,chatWithUs,callUs,shareFeedback,about;
    Context youContext;

    public static YouFragment newInstance() {
        // Required empty public constructor
        YouFragment fragment = new YouFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        youContext = this.getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_you, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_you_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        if(((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fbAuth = FirebaseAuth.getInstance();
        mTitleTextView = (TextView) view.findViewById(R.id.you_title);
        mTitleTextView.setText(HomeFragment.mwelcomeMessageTxtView.getText());
        mLogOut = (TextView) view.findViewById(R.id.logOutTextView);
        profile = (TextView)view.findViewById(R.id.profile);
        faq = (TextView)view.findViewById(R.id.faq);
        chatWithUs = (TextView)view.findViewById(R.id.chatWithUs);
        callUs = (TextView)view.findViewById(R.id.callUs);
        shareFeedback = (TextView)view.findViewById(R.id.shareFeedback);
        about = (TextView)view.findViewById(R.id.about);

        chatWithVendor = (TextView) view.findViewById(R.id.chatWithVendor);

        profile.setClickable(true);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CommonActivity.class);
                startActivity(intent);
            }
        });

        faq.setClickable(true);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CommonActivity.class);
                startActivity(intent);
            }
        });

        chatWithUs.setClickable(true);
        chatWithUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = "+91 8209595522";
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        callUs.setClickable(true);
        callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = "+91 8209595522";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);

            }
        });

        shareFeedback.setClickable(true);
        shareFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CommonActivity.class);
                startActivity(intent);
            }
        });

        about.setClickable(true);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),CommonActivity.class);
                startActivity(intent);

            }
        });

        chatWithVendor.setClickable(true);
        chatWithVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),chatsList.class);
                startActivity(intent);

            }
        });

        mLogOut.setClickable(true);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //user signed out

                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    fbAuth.signOut();
                }

                youContext.getSharedPreferences("User",MODE_PRIVATE)
                        .edit()
                        .putString("UserUid",null)
                        .apply();

                youContext.getSharedPreferences("UserName",MODE_PRIVATE)
                        .edit()
                        .putString("Name",null)
                        .apply();

                Intent intent = new Intent(getActivity(),FirstRunSecondActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

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

              /* case R.id.logOutTextView:
                fbAuth.signOut();
                Intent intent = new Intent(getActivity(),FirstRunSecondActivity.class);
                startActivity(intent);*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}