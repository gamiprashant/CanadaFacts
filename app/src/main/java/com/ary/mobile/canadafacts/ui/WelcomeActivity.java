package com.ary.mobile.canadafacts.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.ary.mobile.canadafacts.R;


public class WelcomeActivity extends Activity {

    //////////////////////////////////////////////////////////////////
    //Activity Methods
    //////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Just running bit of delay for the banner,
        //In future any initialization tasks can be run in this time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchNextActivity();
            }
        }, 2000);
    }

    //////////////////////////////////////////////////////////////////
    void launchNextActivity() {
        Intent i = new Intent();
        i.setClass(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
