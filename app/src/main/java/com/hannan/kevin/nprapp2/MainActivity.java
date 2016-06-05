package com.hannan.kevin.nprapp2;

import android.accounts.AccountManager;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;





public class MainActivity extends AppCompatActivity {

    public static final String API_BASE_URL = "https://api.npr.org/authorization/v2/authorize";

    private static final String TAG = "MainActivity";

    public static String OAUTH_URL = "https://api.npr.org/authorization/v2/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://api.npr.org/authorization/v2/token";

    public static String CLIENT_ID = "nprone_trial_NFUsXLna6ZQu";
    public static String CLIENT_SECRET = "wVSmR4MdHbxMbBGaH2DnMKOf8LYr09J2KCC2stNB";
    public static String CALLBACK_URL = "http://localhost/Callback";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = OAUTH_URL + "?client_id=" + CLIENT_ID
                + "&state=xyz" + "&redirect_uri=" + CALLBACK_URL
                + "&response_type=code" + "&scope=identity.readonly";

        Log.v(TAG, "request url " + url);

        WebView webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String accessTokenFragment = "access_token";
                String accessCodeFragment = "code=";

                Log.v(TAG, "onPageStarted() ");

                // We hijack the GET request to extract the OAuth parameters

                if (url.contains(accessTokenFragment)) {
                    // the GET request contains directly the token

                    Log.v(TAG, "in token branch ");

                    Uri uri = Uri.parse(url);
                    String accessToken = uri.getQueryParameter("access_token");
                    Log.v(TAG, "token " + accessToken);


                } else if(url.contains(accessCodeFragment)) {
                    // the GET request contains an authorization code

                    Log.v(TAG, "in access code branch ");
                    Uri uri = Uri.parse(url);
                    String accessCode = uri.getQueryParameter("code");

                    //TokenStorer.setAccessCode(accessCode);
                    Log.v(TAG, "access code " + accessCode);


                    //String query = "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + accessCode;
                    String query = "grant_type=authorization_code" +
                            "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
                            + "&code=" + accessCode +"&redirect_uri=" + CALLBACK_URL;
                    view.postUrl(OAUTH_ACCESS_TOKEN_URL, query.getBytes());
                }

            }
        });
        webview.loadUrl(url);
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
