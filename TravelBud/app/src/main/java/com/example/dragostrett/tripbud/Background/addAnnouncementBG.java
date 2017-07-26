package com.example.dragostrett.tripbud.Background;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;

/**
 * Created by DragosTrett on 02.06.2017.
 * add anouncement to the trip
 */

public class addAnnouncementBG extends AsyncTask<String, Integer, String> {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... params) {
        String ana=params[0];
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("INSERT INTO `"+ UserInfo.getTrip()+"` (time, announcement) VALUES (?,?)");
            Calendar cal=Calendar.getInstance();
            ps.setString(1, cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+";"+cal.get(Calendar.DAY_OF_MONTH)+","+cal.get(Calendar.MONTH));//add time of the anouncement
            ps.setString(2, ana);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
