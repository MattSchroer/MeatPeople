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
    public static String TAG = "MeatPeople";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "dqUWztAqdE4HmRBkfeWeguyvoqIRIp7Lt8eUJTHc", "6ZxfKEpXUVoOg2AZyVRWIegyoz1zuEleP9xvI3sZ");
        ParseFacebookUtils.initialize("668927893188791");
    }
}

