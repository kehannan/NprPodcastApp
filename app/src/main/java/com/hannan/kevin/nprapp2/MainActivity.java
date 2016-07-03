package com.hannan.kevin.nprapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hannan.kevin.api.PodcastFetchService;
import com.hannan.kevin.login.LoginActivity;
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

        // if not logged in, start the login activity
        if (!manager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        TextView tokenView = (TextView) findViewById(R.id.token_view);

        String token = manager.getToken();

        tokenView.setText(token);

        Button getPodcastButton = (Button) findViewById(R.id.get_podcast_button);
        getPodcastButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PodcastFetchService.class);
                startService(intent);
            }
        });

        Log.v(TAG, "token " + token);

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
