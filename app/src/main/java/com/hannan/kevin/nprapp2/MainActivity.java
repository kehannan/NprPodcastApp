package com.hannan.kevin.nprapp2;

import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

        private static final String TAG = "MainActivity";

        public static String OAUTH_URL = "https://api.npr.org/authorization/v2/authorize";


         private final String clientSecret = "wVSmR4MdHbxMbBGaH2DnMKOf8LYr09J2KCC2stNB";
         private final String redirectUri = "http://localhost/Callback";

        public static String CLIENT_ID = "nprone_trial_NFUsXLna6ZQu";
    public static String CLIENT_SECRET = "mUCJZldRkHjDnO5LIR5HuuDiindpPqVbq7BKcno3";

    public static String CALLBACK_URL = "myapp://callback";

    private static String SCOPE = "identity.readonly identity.write " +
            "listening.readonly listening.write localactivation";

    private static String GRANT_TYPE = "authorization_code";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_main2);

            LoginService client = ServiceGenerator.createService(LoginService.class);

             Button loginButton = (Button) findViewById(R.id.loginbutton);

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

                Call<AccessToken>  call = client.getAccessToken(
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
                            Log.v(TAG, "token=" + token);
                        }

                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.v(TAG, "onFailure ");
                    }
                });

//                try {
//                    AccessToken accessToken = call.execute().body();
//
//                    Log.v(TAG, "AccessToken " + accessToken.getAccessToken());
//                } catch (IOException e) {
//                    Log.v(TAG, "IOException");
//                }


            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }

//    public static final String API_BASE_URL = "https://api.npr.org/authorization/v2/authorize";
//
//    private static final String TAG = "MainActivity";
//
//    public static String OAUTH_URL = "https://api.npr.org/authorization/v2/authorize";
//    public static String OAUTH_ACCESS_TOKEN_URL = "https://api.npr.org/authorization/v2/token";
//
//    public static String CLIENT_ID = "nprone_trial_NFUsXLna6ZQu";
//    public static String CLIENT_SECRET = "wVSmR4MdHbxMbBGaH2DnMKOf8LYr09J2KCC2stNB";
//    public static String CALLBACK_URL = "http://localhost/Callback";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        String url = OAUTH_URL + "?client_id=" + CLIENT_ID
//                + "&state=xyz" + "&redirect_uri=" + CALLBACK_URL
//                + "&response_type=code" + "&scope=identity.readonly";
//
//        Log.v(TAG, "request url " + url);
//
//        WebView webview = (WebView)findViewById(R.id.webview);
//        webview.getSettings().setJavaScriptEnabled(true);
//
//        webview.setWebViewClient(new WebViewClient() {
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                String accessTokenFragment = "access_token";
//                String accessCodeFragment = "code=";
//
//                Log.v(TAG, "onPageStarted() ");
//
//                // We hijack the GET request to extract the OAuth parameters
//
//                if (url.contains(accessTokenFragment)) {
//                    // the GET request contains directly the token
//
//                    Log.v(TAG, "in token branch ");
//
//                    Uri uri = Uri.parse(url);
//                    String accessToken = uri.getQueryParameter("access_token");
//                    Log.v(TAG, "token " + accessToken);
//
//
//                } else if(url.contains(accessCodeFragment)) {
//                    // the GET request contains an authorization code
//
//                    Log.v(TAG, "in access code branch ");
//                    Uri uri = Uri.parse(url);
//                    String accessCode = uri.getQueryParameter("code");
//
//                    //TokenStorer.setAccessCode(accessCode);
//                    Log.v(TAG, "access code " + accessCode);
//
//
//                    //String query = "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + accessCode;
//                    String query = "grant_type=authorization_code" +
//                            "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
//                            + "&code=" + accessCode +"&redirect_uri=" + CALLBACK_URL;
//                    view.postUrl(OAUTH_ACCESS_TOKEN_URL, query.getBytes());
//                }
//
//            }
//        });
//        webview.loadUrl(url);
//    }

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
