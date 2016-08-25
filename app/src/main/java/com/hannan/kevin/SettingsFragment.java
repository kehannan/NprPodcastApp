package com.hannan.kevin;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hannan.kevin.login.SessionManager;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    SessionManager manager;

    TextView mTextView;
    Button mLogoutButton;
    View rootView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        if (manager.isLoggedIn()) {

            mTextView = (TextView) rootView.findViewById(R.id.settings_text_view);
            mLogoutButton = (Button) rootView.findViewById(R.id.logout_button);

            mLogoutButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    manager.logout();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        }
        return rootView;
    }
}
