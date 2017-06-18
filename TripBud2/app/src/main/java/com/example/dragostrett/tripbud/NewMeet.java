package com.example.dragostrett.tripbud;

import android.content.Context;
import android.os.AsyncTask;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 25.05.2017.
 */

public class NewMeet extends AsyncTask<String, Integer, String> {
    Context context;
    public NewMeet(Context context){this.context=context;}
    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("INSERT INTO meet (moment, latitudine, longitudine) VALUES (?,?,?)");
            ps.setString(1, TripInfo.getMeet());
            ps.setString(2, MeetInfo.getLatitudine());
            ps.setString(3, MeetInfo.getLongitudine());
            ps.executeUpdate();
            ps= (PreparedStatement) con.prepareStatement("UPDATE trips SET meet=? WHERE name=?");
            ps.setString(1, TripInfo.getMeet());
            ps.setString(2, UserInfo.getTrip());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){

    }
}
