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

public class AddUserBG extends AsyncTask<String, Integer, String> {
    Context context;
    public AddUserBG(Context context){this.context=context;}
    @Override
    protected String doInBackground(String... params) {
        String username=params[0];
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET trip=? WHERE username=?");
            ps.setString(1, TripInfo.getNameTrip());
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
            Toast.makeText(context, "User added",
                    Toast.LENGTH_SHORT).show();

    }
}
