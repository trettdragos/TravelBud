package com.example.dragostrett.tripbud.Background;

/**
 * Created by DragosTrett on 23.05.2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dragostrett.tripbud.BasicInfo.MeetInfo;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.example.dragostrett.tripbud.MainActivity;
import com.example.dragostrett.tripbud.Security.BCrypt;
import com.google.android.gms.maps.model.LatLng;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class loginBG extends AsyncTask<String, Integer, String> {
    Context context;
    public loginBG(Context context){
        this.context=context;
    }
    @Override
    protected String doInBackground(String... params) {
        String user=params[0];
        String pass=params[1];
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("SELECT * FROM table1 WHERE username=?");
            ps.setString(1, user);
            //ps.setString(2, pass);
            rs=ps.executeQuery();
            if(!rs.isBeforeFirst()){
                UserInfo.setLogedIn(false);
            }
            else{
                while(rs.next()){
                    if(!BCrypt.checkpw(pass, rs.getString("password"))){
                        UserInfo.setLogedIn(false);
                        return null;
                    }else{
                        UserInfo.setLogedIn(true);
                        UserInfo.setUsername(rs.getString("username"));
                        UserInfo.setEmail(rs.getString("email"));
                        UserInfo.setPassword(pass);
                        UserInfo.setUserLoc(new LatLng(Double.parseDouble(rs.getString("latitudine")), Double.parseDouble(rs.getString("longitudine"))));
                        UserInfo.setId(rs.getString("id"));
                        UserInfo.setTrip(rs.getString("trip"));
                        UserInfo.setType(rs.getString("type"));
                        String aux=rs.getString("visible");
                        if(aux.equals("1"))
                            UserInfo.setVisible(true);
                        else UserInfo.setVisible(false);
                        UserInfo.setNotification(rs.getString("notificare"));
                        UserInfo.setLocation(true);
                    }
                }
            }
            if(UserInfo.getTrip().equals("")){
                TripInfo.setMeet("");
                TripInfo.setInATrip(false);
            }
            else{
                ps= (PreparedStatement) con.prepareStatement("SELECT * FROM trips WHERE name='"+UserInfo.getTrip()+"'");
                rs=ps.executeQuery();
                TripInfo.setInATrip(true);
                while(rs.next()){
                    TripInfo.setCircleCenter(new LatLng(Double.parseDouble(rs.getString("LatitudineCentru")), Double.parseDouble(rs.getString("LongitudineCentru"))));
                    TripInfo.setCircleRange(Integer.parseInt(rs.getString("circleRange")));
                    TripInfo.setIdTrip(rs.getString("id"));
                    TripInfo.setNameTrip(rs.getString("name"));
                    TripInfo.setPlace(rs.getString("place"));
                    TripInfo.setOrganizator(rs.getString("organizator"));
                    TripInfo.setMeet(rs.getString("meet"));
                    TripInfo.setNumber_users(rs.getString("number_users"));
                    TripInfo.setStartDate(rs.getDate("startDate"));
                    TripInfo.setEndDate(rs.getDate("endDate"));
                }
            }
            if(!TripInfo.getMeet().equals("")){
                ps= (PreparedStatement) con.prepareStatement("SELECT * FROM meet WHERE  moment='"+TripInfo.getMeet()+"'");
                rs=ps.executeQuery();
                while(rs.next()){
                    MeetInfo.setPosition(new LatLng(Double.parseDouble(rs.getString("latitudine")), Double.parseDouble(rs.getString("longitudine"))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "done";
    }

    @Override
    protected void onPostExecute(String result){
        if(UserInfo.isLogedIn()){
            UserInfo.setLogedIn(true);
            MainActivity.k=true;
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
        else{
            Toast.makeText(context, "Incorrect username or password",
                    Toast.LENGTH_SHORT).show();
            UserInfo.setLogedIn(false);
        }
        if(UserInfo.getCurentDate().before(TripInfo.getStartDate()) || UserInfo.getCurentDate().after(TripInfo.getEndDate())){
            UserInfo.setShowEveryThing(false);
        }
        else
            UserInfo.setShowEveryThing(true);
    }

}