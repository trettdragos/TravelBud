package com.example.dragostrett.tripbud;

import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

/**
 * Created by DragosTrett on 02.06.2017.
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
            ps= (PreparedStatement) con.prepareStatement("INSERT INTO `"+UserInfo.getTrip()+"` (time, announcement) VALUES (?,?)");
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            ps.setString(1, String.valueOf(currentDateTimeString));
            ps.setString(2, ana);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
