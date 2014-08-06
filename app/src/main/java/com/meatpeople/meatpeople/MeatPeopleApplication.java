package com.meatpeople.meatpeople;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;

/**
 * Created by Matt on 8/6/2014.
 */
public class MeatPeopleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "dqUWztAqdE4HmRBkfeWeguyvoqIRIp7Lt8eUJTHc", "6ZxfKEpXUVoOg2AZyVRWIegyoz1zuEleP9xvI3sZ");
        ParseFacebookUtils.initialize("668927893188791");
        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

        // other fields can be set just like with ParseObject
        user.put("phone", "650-555-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}

