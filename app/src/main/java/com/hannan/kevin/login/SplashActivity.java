package com.hannan.kevin.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hannan.kevin.nprapp2.MainActivity;
import com.hannan.kevin.nprapp2.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.v(TAG, "onCreate()");
        manager = new SessionManager(this);

        // METHOD 1

        /****** Create Thread that will sleep for 3 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 3 seconds
                    sleep(3*1000);

                    // After 5 seconds redirect to another intent
                    String status=manager.getPreferences("status");
                    Log.d("status",status);
                    if (status.equals("1")){
                        Intent i=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(i);
                    }else{
                        Intent i=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

//METHOD 2

        /*
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
              if (status=="1"){
                        Intent i=new Intent(Splash.this,MainActivity.class);
                        startActivity(i);
                    }else{
                        Intent i=new Intent(Splash.this,Login.class);
                        startActivity(i);
                    }

                // close this activity
                finish();
            }
        }, 3*1000); // wait for 3 seconds
        */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
