package com.example.jingqiuzhou.spot;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class AddFriendActivity extends ActionBarActivity {
    private EditText mEmailEditText;
    private Button mAddFriendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend);

        mEmailEditText = (EditText)findViewById(R.id.new_friend_email_edit_text);
        mAddFriendButton = (Button)findViewById(R.id.add_new_friend_button);
        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = mEmailEditText.getText().toString().trim();
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("email", emailAddress);
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser == null) {
                            Toast.makeText(
                                    AddFriendActivity.this,
                                    "Can't find user with this email",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            ParseRelation<ParseUser> relation = currentUser.getRelation("friends");
                            relation.add(parseUser);
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(
                                                AddFriendActivity.this,
                                                "Successfully added new friend",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            ParseRelation<ParseUser> friendRelation = parseUser.getRelation("friends");
                            friendRelation.add(currentUser);
                            parseUser.saveInBackground();
                        }
                    }
                });
            }
        });
    }
}
