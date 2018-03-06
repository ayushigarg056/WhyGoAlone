package com.ayushi.user.restaurantapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ActivitySplash extends Activity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i;
                    //auth = FirebaseAuth.getInstance();
//                    Log.d("WSS", "run: " + auth);
//                    if (auth.getCurrentUser() != null) {
//                        i = new Intent(ActivitySplash.this, DashboardActivity.class);
//                    } else {
//                        i = new Intent(ActivitySplash.this, MainActivity.class);
//                    }
                    i = new Intent(ActivitySplash.this, MainActivity.class);
//
                    startActivity(i);
                    finish();
                }
            }, 1000);
        }

}

