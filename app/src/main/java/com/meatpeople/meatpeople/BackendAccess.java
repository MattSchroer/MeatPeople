package com.meatpeople.meatpeople;

import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;

/**
 * Created by Matt on 8/7/2014.
 */
public class BackendAccess {

    @ParseClassName("Event")
    public class Event extends ParseObject {
        public String name;
        public Date date;
        public String type;
        public String eventId;
        public String ownerId;

    }

    public class Response {
        public boolean attending;
        public String userId;
        public String eventId;
    }

    // Use this to make new event
    public static void AddEvent(Event incoming) {
        String id = ParseUser.getCurrentUser().getObjectId();
        final ParseObject parseEvent = new ParseObject("Event");
        parseEvent.put("name", incoming.name);
        parseEvent.put("eventDate", incoming.date);
        parseEvent.put("type", incoming.type);
        parseEvent.put("owner", id);
        parseEvent.saveInBackground((new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    String eventId = parseEvent.getObjectId();
                    AddResponse(eventId, true);
                } else {
                    // TODO: figure out failure logic.
                }
            }
        }));
    }

    public static void AddResponse(String eventId, boolean attending) {
        String id = ParseUser.getCurrentUser().getObjectId();
        final ParseObject inviteResponse = new ParseObject("Response");
        inviteResponse.put("eventId", eventId);
        inviteResponse.put("attending", attending);
        inviteResponse.put("userId", id);
        if (attending) {
            ParseGeoPoint location = GetLocation();
            inviteResponse.put("location", location);
        }

        inviteResponse.saveInBackground((new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    //TODO: put whatever goes after response creation here. Return to events list.
                } else {
                    //TODO: add failure logic.
                }
            }
        }));
    }

    private static ParseGeoPoint GetLocation() {
        //TODO: add real location logic.
        Location location = new Location("");
        ParseGeoPoint geoPt = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        return geoPt;
    }
}
