package com.hannan.kevin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hannan.kevin.api.PodcastFetchService;
import com.hannan.kevin.login.LoginActivity2;
import com.hannan.kevin.login.SessionManager;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate()");

        manager = new SessionManager(this);

        Log.v(TAG, "token " + manager.getToken());
        Log.v(TAG, "isLoggedin? " + manager.isLoggedIn());

        // if not logged in, start the login activity
        if (!manager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity2.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
