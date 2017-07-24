package com.example.dragostrett.tripbud.Background;

import android.content.Context;
import android.os.AsyncTask;

import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 26.05.2017.
 */

public class UpdateLocationBG extends AsyncTask<String, Integer, String> {
    Context context;
    public UpdateLocationBG(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET longitudine=?, latitudine=? WHERE username=?");
            ps.setString(1, String.valueOf(UserInfo.getUserLoc().longitude));
            ps.setString(2, String.valueOf(UserInfo.getUserLoc().latitude));
            ps.setString(3, UserInfo.getUsername());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
