package com.example.dragostrett.tripbud;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 25.05.2017.
 */

public class DeleteMeet extends AsyncTask<String, Integer, String> {
    Context context;
    public DeleteMeet(Context context){this.context=context;}
    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection("jdbc:mysql://35.187.169.134:3306/android", "user", "password");
            ps= (PreparedStatement) con.prepareStatement("UPDATE trips SET meet=? WHERE name=?");
            ps.setString(1, "");
            ps.setString(2, UserInfo.getTrip());
            ps.executeUpdate();
            ps= (PreparedStatement) con.prepareStatement("DELETE FROM meet WHERE moment=?");
            ps.setString(1, TripInfo.getMeet());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        Toast.makeText(context, "Meet Delete",
                Toast.LENGTH_SHORT).show();
        TripInfo.setMeet("");
        MainActivity.meet.remove();

    }
}
