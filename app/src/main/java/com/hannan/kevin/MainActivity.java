package com.hannan.kevin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hannan.kevin.api.LoginService;
import com.hannan.kevin.api.ServiceGenerator;
import com.hannan.kevin.login.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
    implements PodcastSummaryFragment.Callback, LoginDialog.LoginDialogListener {

    public static String OAUTH_URL = "https://api.npr.org/authorization/v2/authorize";

    public static String CLIENT_ID = "nprone_trial_NFUsXLna6ZQu";
    public static String CLIENT_SECRET = BuildConfig.NPR_SECRET_KEY;

    public static String CALLBACK_URL = "myapp://callback";

    private static String SCOPE = "identity.readonly identity.write " +
            "listening.readonly listening.write localactivation";

    private static String GRANT_TYPE = "authorization_code";

    final String url = OAUTH_URL + "?client_id=" + CLIENT_ID
            + "&state=xyz" + "&redirect_uri=" + CALLBACK_URL
            + "&response_type=code" + "&scope=" + SCOPE;

    private static final String TAG = "MainActivity";

    SessionManager manager;
    LoginDialog loginDialog;

    private static final String IS_LOADING = "is_loading";
    private SharedPreferences mPreferences;

    LoginInterface loginInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        manager = new SessionManager(this);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loginInterface = (LoginInterface)
                getSupportFragmentManager().findFragmentById(R.id.summary_fragment);

        Log.v(TAG, "onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.v(TAG, "onResume()");

        // if login is not loading show the dialog
        showDialogIfNotLoad();


        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {

            String code = uri.getQueryParameter("code");
            if (code != null) {

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

                        if (response.isSuccessful()) {
                            String token = response.body().getAccessToken();

                            manager.setToken(token);
                            Log.v(TAG, "token=" + token);

                            loginInterface.onLogin();
                        }

                        setIsLoading(false);
                        showDialogIfNotLoad();
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.v(TAG, "onFailure ");

                        setIsLoading(false);
                        showDialogIfNotLoad();
                    }
                });
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()");
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

    // implements interface method to get data from PodcastSummaryFragment
    @Override
    public void onItemSelected(Uri uri) {
        Log.v(TAG, "onItemSelected " + uri);

        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(PodcastSummaryFragment.PODCAST_ID, uri.toString());
        startActivity(i);
    }

    @Override
    public void onClickLogin() {
        Log.v(TAG, "onClickLogin()");

        // on click, dismiss the dialog and set isLoading flag
        loginDialog.dismiss();
        setIsLoading(true);

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                        url));

        startActivity(intent);
    }

    // check if login is loading
    private boolean isLoading() {
        boolean result = mPreferences.getBoolean(IS_LOADING, false);
        return result;
    }

    // set whether login is loading or not
    private void setIsLoading(boolean isLoading) {
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putBoolean(IS_LOADING, isLoading);
        editor.commit();
    }

    // show the login dialog if not loading
    private void showDialogIfNotLoad() {

        Log.v(TAG, "showDialogIfNotLoad");

        // show if it's not loading and if user is not logged in
        if (!isLoading() & !manager.isLoggedIn()) {
            loginDialog = new LoginDialog();
            loginDialog.show(getSupportFragmentManager(), "dialog");
        }
    }
}
