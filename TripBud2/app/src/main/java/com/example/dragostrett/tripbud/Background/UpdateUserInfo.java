package com.example.dragostrett.tripbud.Background;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.example.dragostrett.tripbud.MainActivity;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 25.05.2017.
 */

public class UpdateUserInfo extends AsyncTask<String, Integer, String> {
    Context context;
    public UpdateUserInfo(Context context){this.context=context;}
    @Override
    protected String doInBackground(String... params) {
        String username=params[0];
        String password=params[1];
        String email=params[2];
        String vis=params[3];
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET username=?, password=?, email=?, visible=? WHERE username=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, vis);
            ps.setString(5, UserInfo.getUsername());
            ps.executeUpdate();
            UserInfo.setUsername(username);
            UserInfo.setPassword(password);
            UserInfo.setEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        MainActivity.start();
            Toast.makeText(context, "Changes saved",
                    Toast.LENGTH_SHORT).show();

    }
}
