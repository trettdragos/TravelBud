package com.example.dragostrett.tripbud;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dragostrett.tripbud.Background.loginBG;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.example.dragostrett.tripbud.Security.BCrypt;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LogInActivity extends AppCompatActivity
       // implements EasyPermissions.PermissionCallbacks
{


    GoogleAccountCredential mCredential;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR };

    public static final String PREFS_NAME = "RemeberMeFile";
    EditText username;
    EditText password;
    static int mNotificationId = 001;
    static  Context context;
    public static String pass="";
    CheckBox rememberMe, loggedIn ;
    public static SharedPreferences pref, prefAutoLogIn;
    public static Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        username = (EditText)findViewById(R.id.editText_name);
        password = (EditText)findViewById(R.id.editText_password);
        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String u=pref.getString("username", null);
        String p = pref.getString("password", null);
        Boolean autoLogIn=pref.getBoolean("autoLogIn", false);
        username.setText(u);
        password.setText(p);
        if(autoLogIn){
            if(UserInfo.isAutoLogIn()){
                UserInfo.setLogedIn(false);
                if(isOnline())
                new loginBG(this).execute(u, p);
                else Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG);
            }else{
                pref.edit().putString("username", UserInfo.getUsername()).putString("password", UserInfo.getPassword()).putBoolean("autoLogIn", false).commit();
            }
        }
        fa=this;
        context = this;
        rememberMe=(CheckBox)findViewById(R.id.checkBoxRemember);
        loggedIn=(CheckBox)findViewById(R.id.checkBoxLoggedIn);
        loggedIn.setChecked(true);
        rememberMe.setChecked(true);
        cancelNotification();
        /*NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.x)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setOngoing(true)
                        .setAutoCancel(false)
                        .setPriority(Notification.PRIORITY_MIN)
                        .setDefaults(Notification.DEFAULT_ALL);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());*/
        cal= Calendar.getInstance();
        UserInfo.setCurentDate(new Date(cal.get(Calendar.YEAR)-1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
        TripInfo.setStartDate(new Date(0, 0, 0));
        TripInfo.setEndDate(new Date(0, 0, 0));
        UserInfo.setShowEveryThing(false);

        //TODO test for google calendar api
        /*mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        getResultsFromApi();*/
    }

    private void getResultsFromApi() {
        Log.e("calendar", "events testing");
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isOnline()) {
            Log.e("calendar", "No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
            Log.e("calendar", "showing events");
        }
    }

    public void loginUser(View view) throws InterruptedException {
        if(isOnline()){
            if(username.getText().equals("") || password.getText().equals("")){
                Toast.makeText(this, "Please enter username and password",
                        Toast.LENGTH_SHORT).show();
                return;
            } String ps=password.getText().toString(), un=username.getText().toString();
            if(rememberMe.isChecked()) {
                if(loggedIn.isChecked())
                pref.edit().putString("username", un).putString("password", ps).putBoolean("autoLogIn", true).commit();
                else pref.edit().putString("username", un).putString("password", ps).putBoolean("autoLogIn", false).commit();
            }else if(!pref.getString("username", null).equals(un)){
                pref.edit().putString("username", "").putString("password", "").putBoolean("autoLogIn", false).commit();
            }
            pass=ps;
            UserInfo.setLogedIn(false);
            Log.e(pass, BCrypt.hashpw(pass, BCrypt.gensalt()));
            new loginBG(this).execute(un, ps);
        }
        else{
            Toast.makeText(this, "No internet acces",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void openRegisterAct(View view){
        if(isOnline()){
            Intent intent = new Intent(this, RegisterActivity.class);
            this.startActivity(intent);
        }
        else{
            Toast.makeText(this, "No internet acces",
                    Toast.LENGTH_SHORT).show();
        }

        //cancelNotification();

    }
    public static Activity fa;
    public boolean isOnline(){
        ConnectivityManager c=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=c.getActiveNetworkInfo();
        return info !=null && info.isConnectedOrConnecting();
    }
    public static void cancelNotification()
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(mNotificationId);
    }

    //@Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    //@Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                LogInActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                eventStrings.add(
                        String.format("%s (%s)", event.getSummary(), start));
            }
            return eventStrings;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(List<String> output) {
            if (output == null || output.size() == 0) {
               Log.e("calendar","No results returned.");
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                Log.e("calendar", String.valueOf(TextUtils.join("\n", output)));
            }
        }

        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            LogInActivity.REQUEST_AUTHORIZATION);
                } else {
                    Log.e("calendar","The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Log.e("calendar","Request cancelled.");
            }
        }
    }
}
