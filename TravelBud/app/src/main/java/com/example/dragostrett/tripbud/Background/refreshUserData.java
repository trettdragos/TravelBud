package com.example.dragostrett.tripbud.Background;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dragostrett.tripbud.BasicInfo.MeetInfo;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.example.dragostrett.tripbud.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Created by DragosTrett on 08.07.2017.
 */

public class refreshUserData extends AsyncTask<String, Integer, String> {
    Context context;
    public static String notificationCheck="";
    GoogleMap mapP;
    public refreshUserData(Context context, GoogleMap mapP){
        this.context=context;
        this.mapP=mapP;
    }
    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("SELECT * FROM table1 WHERE username=?");
            ps.setString(1, UserInfo.getUsername());
            rs=ps.executeQuery();
            if(!rs.isBeforeFirst()){
                UserInfo.setLogedIn(false);
            }
            else{
                UserInfo.setLogedIn(true);
                while(rs.next()){
                    UserInfo.setTrip(rs.getString("trip"));
                    UserInfo.setType(rs.getString("type"));
                    String aux=rs.getString("visible");
                    if(aux.equals("1"))
                        UserInfo.setVisible(true);
                    else UserInfo.setVisible(false);
                    notificationCheck=rs.getString("notificare");
                    //UserInfo.setNotification(rs.getString("notificare"));
                    UserInfo.setLocation(true);
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
                    MeetInfo.setLongitudine(rs.getString("longitudine"));
                    MeetInfo.setLatitudine(rs.getString("latitudine"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "done";
    }
    @Override
    protected void onPostExecute(String result){

        if(!UserInfo.getNotification().equals(notificationCheck)){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.x)
                            .setContentTitle("New Join Request")
                            .setContentText("A request to join"+notificationCheck+" has been sent to you")
                            .setOngoing(false)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL);

            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(2, mBuilder.build());
        }
        UserInfo.setNotification(notificationCheck);
        notificationCheck="";

        if(UserInfo.getCurentDate().before(TripInfo.getStartDate()) || UserInfo.getCurentDate().after(TripInfo.getEndDate())){
            UserInfo.setShowEveryThing(false);
        }
        else{
            UserInfo.setShowEveryThing(true);
            Log.e("test", "Show everything");
        }

    }
}
