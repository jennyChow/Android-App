package com.example.jingqiuzhou.spot;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {

    private TextView mSpottingUserTextView;
    private TextView mAccountNameTextView;
    private TextView mDisplayNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAccountNameTextView = (TextView)getActivity().findViewById(R.id.profile_username);
        mDisplayNameTextView = (TextView)getActivity().findViewById(R.id.profile_display_name);
        ParseUser currentUser = ParseUser.getCurrentUser();
        mAccountNameTextView.setText(currentUser.getUsername());
        mDisplayNameTextView.setText(currentUser.getString("name"));

        ParseUser spottedFriend = currentUser.getParseUser("spotUser");
        mSpottingUserTextView = (TextView)getActivity().findViewById(R.id.spotting_name);
        if (spottedFriend == null) {
            mSpottingUserTextView.setText("You're not spotting anyone!");
        } else {
            spottedFriend.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        mSpottingUserTextView.setText(((ParseUser)parseObject).getString("name"));
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
