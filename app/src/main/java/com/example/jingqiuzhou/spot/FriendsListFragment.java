package com.example.jingqiuzhou.spot;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class FriendsListFragment extends Fragment {
    private ListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friends_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mListView = (ListView)getActivity().findViewById(R.id.friends_list);

        ParseQueryAdapter.QueryFactory<ParseUser> factory =
                new ParseQueryAdapter.QueryFactory<ParseUser>() {
                    @Override
                    public ParseQuery<ParseUser> create() {
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        ParseRelation<ParseUser> relation = currentUser.getRelation("friends");
                        return relation.getQuery();
                    }
                };

        ParseQueryAdapter<ParseUser> adapter =
            new ParseQueryAdapter<ParseUser>(getActivity(), factory) {
                @Override
                public View getItemView(final ParseUser parseUser, View view, ViewGroup parent) {
                    if (view == null) {
                        view = View.inflate(getActivity(), R.layout.friend_list_item, null);
                    }
                    TextView nameTextView = (TextView)view.findViewById(R.id.friend_name);
                    nameTextView.setText(parseUser.getString("name"));


                    Button spotButton = (Button)view.findViewById(R.id.spot_button);
                    ParseUser currentSpottedUser = (ParseUser)ParseUser.getCurrentUser().get("spotUser");

                    if (currentSpottedUser != null &&
                        currentSpottedUser.getUsername() == parseUser.getUsername()) {
                        spotButton.setEnabled(false);
                    }

                    spotButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ParseUser currentUser = ParseUser.getCurrentUser();

                            currentUser.put("spotUser", parseUser);
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(),
                                                "Spot sccessfully!", Toast.LENGTH_SHORT).show();
                                        mListView.invalidateViews();

                                    } else {
                                        Toast.makeText(getActivity(),
                                                "Spot failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    Button unfriendButton = (Button)view.findViewById(R.id.unfriend_button);
                    unfriendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            ParseRelation<ParseUser> relation = currentUser.getRelation("friends");
                            relation.remove(parseUser);
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    notifyDataSetChanged();
                                }
                            });

                            ParseRelation<ParseUser> friendRelation = parseUser.getRelation("friends");
                            friendRelation.remove(currentUser);
                            parseUser.saveInBackground();

                        }
                    });

                    return view;
                }
            };
        mListView.setAdapter(adapter);
    }
}
