package com.hannan.kevin.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hannan.kevin.AccessToken;
import com.hannan.kevin.BuildConfig;
import com.hannan.kevin.MainActivity;
import com.hannan.kevin.R;
import com.hannan.kevin.api.LoginService;
import com.hannan.kevin.api.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity2 extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    public static String OAUTH_URL = "https://api.npr.org/authorization/v2/authorize";

    public static String CLIENT_ID = "nprone_trial_NFUsXLna6ZQu";
    public static String CLIENT_SECRET = BuildConfig.NPR_SECRET_KEY;

    public static String CALLBACK_URL = "myapp://callback";

    private static String SCOPE = "identity.readonly identity.write " +
            "listening.readonly listening.write localactivation";

    private static String GRANT_TYPE = "authorization_code";

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = new SessionManager(this);

//        LoginService client = ServiceGenerator.createService(LoginService.class);

        Button loginButton = (Button) findViewById(R.id.login_button);

        final String url = OAUTH_URL + "?client_id=" + CLIENT_ID
                + "&state=xyz" + "&redirect_uri=" + CALLBACK_URL
                + "&response_type=code" + "&scope=" + SCOPE;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(TAG, "url " + url);

                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                                url));

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // hack
        if(manager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity2.this, MainActivity.class);
            startActivity(intent);
        }

        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                // we'll do that in a minute

                Log.v(TAG, "access code " + code);

                LoginService client = ServiceGenerator.createService(LoginService.class);

                Call<AccessToken> call = client.getAccessToken(
                        GRANT_TYPE,
                        CLIENT_ID,
                        CLIENT_SECRET,
                        code,
                        CALLBACK_URL);

                call.enqueue(new Callback<AccessToken>() {

                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                        Log.v(TAG, "onResponse()");

                        if (response.isSuccessful()) {
                            String token = response.body().getAccessToken();

                            manager.setToken(token);

                            Log.v(TAG, "token=" + token);

                            Intent intent = new Intent(LoginActivity2.this, MainActivity.class);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.v(TAG, "onFailure ");
                    }
                });
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }
}