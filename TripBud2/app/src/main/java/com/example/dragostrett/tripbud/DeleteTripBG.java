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

public class DeleteTripBG extends AsyncTask<String, Integer, String> {
    Context context;
    public DeleteTripBG(Context context){this.context=context;}
    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET trip=? WHERE trip=?");//update users current trip to ""
            ps.setString(1, "");
            ps.setString(2, UserInfo.getTrip());
            ps.executeUpdate();
            ps= (PreparedStatement) con.prepareStatement("DELETE FROM trips WHERE name=?");//delete the trip from db
            ps.setString(1, UserInfo.getTrip());
            ps.executeUpdate();
            if(!TripInfo.getMeet().equals("")){
                ps= (PreparedStatement) con.prepareStatement("DELETE FROM meet WHERE moment=?");//delete meet from db
                ps.setString(1, TripInfo.getMeet());
                ps.executeUpdate();
            }
            ps= (PreparedStatement) con.prepareStatement("DROP TABLE `"+UserInfo.getTrip()+"`");//delete announcements from DB
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        TripInfo.setInATrip(false);
        Toast.makeText(context, "Trip Deleted",
                Toast.LENGTH_SHORT).show();

    }
}
