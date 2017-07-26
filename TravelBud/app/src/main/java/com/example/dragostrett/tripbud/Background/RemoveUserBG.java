package com.example.dragostrett.tripbud.Background;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 27.05.2017.
 * removes user from trip
 */

public class RemoveUserBG extends AsyncTask<String, Integer, String> {
    Context context;
    public RemoveUserBG(Context context){this.context=context;}
    @Override
    protected String doInBackground(String... params) {
        String user=params[0];
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET trip=? WHERE username=?");
            ps.setString(1, "");
            ps.setString(2, user);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        Toast.makeText(context, "User removed from trip",
                Toast.LENGTH_SHORT).show();

    }
}
