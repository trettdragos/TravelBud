package com.example.dragostrett.tripbud;

import android.os.AsyncTask;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 02.06.2017.
 */

public class CreateAnnouncement extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("CREATE TABLE `"+UserInfo.getTrip()+"` (" +
                    "                      `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "                      `time` VARCHAR(50) NOT NULL," +
                    "                      `announcement` VARCHAR(50) NOT NULL" +
                    "                    )");
            //ps.setString(1, UserInfo.getTrip());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
