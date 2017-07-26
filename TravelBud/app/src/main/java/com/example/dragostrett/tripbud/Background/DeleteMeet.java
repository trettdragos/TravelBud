package com.example.dragostrett.tripbud.Background;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dragostrett.tripbud.MainActivity;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 25.05.2017.
 * deletets the meeting of the coresponding trip in db
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
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
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
        MainActivity.start();
        Toast.makeText(context, "Meet Delete",
                Toast.LENGTH_SHORT).show();
        TripInfo.setMeet("");
        MainActivity.meet.remove();

    }
}
