package com.example.dragostrett.tripbud.Background;

import android.content.Context;
import android.os.AsyncTask;

import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
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
        String join=params[0];
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET trip=?, notificare=? WHERE username=?");
            if(join.equals("1"))
                ps.setString(1, UserInfo.getNotification());
            else ps.setString(1, "");
            ps.setString(2, "");
            ps.setString(3, UserInfo.getUsername());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        UserInfo.setNotification("");

    }
}
