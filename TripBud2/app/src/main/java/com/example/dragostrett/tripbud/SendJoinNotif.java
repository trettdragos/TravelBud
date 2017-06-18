package com.example.dragostrett.tripbud;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

import java.sql.DriverManager;

/**
 * Created by DragosTrett on 18.06.2017.
 */

public class SendJoinNotif extends AsyncTask<String, Integer, String> {
        Context context;

    public SendJoinNotif(Context context){
        this.context=context;
        }
    @Override
    protected String doInBackground(String... params) {
        String user=params[0];
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
        Class.forName("com.mysql.jdbc.Driver");
        con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
        ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET notificare=? WHERE username=?");
        ps.setString(1, UserInfo.getTrip());
        ps.setString(2, user);
        ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return null;
    }

    @Override
    protected void onPostExecute(String result){
        Toast.makeText(context, "A reques was sent to the user to join", Toast.LENGTH_SHORT).show();
    }
}
