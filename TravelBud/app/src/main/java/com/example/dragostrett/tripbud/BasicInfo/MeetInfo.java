package com.example.dragostrett.tripbud.BasicInfo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by DragosTrett on 25.05.2017.
 * class for local saving the meeting point infoemation
 */

public class MeetInfo {
    public static String moment;
    public static LatLng position;

    public static LatLng getPosition() {
        return position;
    }

    public static void setPosition(LatLng position) {
        MeetInfo.position = position;
    }

    public static String getMoment() {
        return moment;
    }

    public static void setMoment(String moment) {
        MeetInfo.moment = moment;
    }

}
