package com.hannan.kevin.login;

/**
 * Created by khannan on 6/22/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private static final String TAG = "SessionManager";

    private static final String NPR_APP = "npr_app";

    private static final String TOKEN_KEY = "token_key";

    private static final String BEARER_TAG = "Bearer "; //note the space

    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        editor = context.getSharedPreferences(NPR_APP, Context.MODE_PRIVATE).edit();
    }

    public void setPreferences(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setToken(String value) {

        Log.v(TAG, "setToken() " + BEARER_TAG + value);

        editor.putString(TOKEN_KEY, BEARER_TAG + value);
        editor.commit();
    }

    public String getToken() {
        SharedPreferences prefs = context.getSharedPreferences(NPR_APP,	Context.MODE_PRIVATE);
        String token = prefs.getString(TOKEN_KEY, "");

        Log.v(TAG, "getToken() " + token);
        return token;
    }

    public boolean isLoggedIn() {
        String token = getToken();
        if (token != "") {
            return true;
        } else {
            return false;
        }
    }

    public  String getPreferences(String key) {

        SharedPreferences prefs = context.getSharedPreferences(NPR_APP, Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }
}

