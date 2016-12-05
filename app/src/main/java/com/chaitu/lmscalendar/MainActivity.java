package com.chaitu.lmscalendar;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaitu.lmscalendar.sakai.sync.AlarmReceiver;
import com.chaitu.lmscalendar.sakai.sync.EventsSyncService;
import com.chaitu.lmscalendar.sakai.util.EventUtil;
import com.chaitu.lmscalendar.sakai.util.SakaiUtil;
import com.chaitu.lmscalendar.settings.Settings;
import com.chaitu.lmscalendar.ui.CalendarModel;
import com.chaitu.lmscalendar.ui.UrlDialog;

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final String[] MY_PERMISSIONS = new String[]{
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.INTERNET
    };
    /**
     * Root of the layout of this Activity.
     */
    private View mLayout;
    private EditText mTextCalendarUrl;
    private EditText mTextUsername;
    private EditText mTextPassword;
    private Spinner mSpinnerSAKTZ;
    private Spinner mSpinnerSyncInterval;
    private Spinner mSpinnerCalendar;
    private Button mSaveButton;
    private TextView mUrlText;
    private TextView mUsernameText;
    private TextView mPasswordText;
    private TextView mSAKTZText;
    private TextView mCalIdText;
    private TextView mSyncIntervalText;
    private Button mEditButton;
    private Settings mSettings;
    private ProgressBar mProgress;
    private TextView mTextSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasPermissions()) {

            setContentView(R.layout.activity_main);
            mProgress = (ProgressBar) findViewById(R.id.progress_bar);
            mProgress.setVisibility(View.GONE);
            mTextSignIn = (TextView) findViewById(R.id.sign_in);
            mTextSignIn.setVisibility(View.GONE);

            mLayout = findViewById(R.id.sample_main_layout);
            mSettings = new Settings(PreferenceManager.getDefaultSharedPreferences(this));
            String url = mSettings.getString(Settings.PREF_LASTURL);
            String username = mSettings.getString(Settings.PREF_LASTURLUSERNAME);
            String password = mSettings.getString(Settings.PREF_LASTURLPASSWORD);
            String timezone = mSettings.getString(Settings.PREF_LASTSAKTZ);
            int calID = mSettings.getInt(Settings.PREF_LASTCALENDARID);
            String calendarName = mSettings.getString(Settings.PREF_LASTCALENDARNAME);
            int syncInterval = mSettings.getInt(Settings.PREF_LASTSYNCINTERVAL);
            Log.i(TAG, "url:"+url+"\tusername:"+username+"\tpassword:"+password+"\tcalID:"+calID+"\tcalendarName:"+calendarName+"\tsyncInterval"+syncInterval);
            if (isDataAvailable(url, username, password, calID, syncInterval,timezone)) {
                //hide the form fields
                mTextCalendarUrl = (EditText) findViewById(R.id.TextCalendarUrl);
                mTextCalendarUrl.setVisibility(View.GONE);
                mTextUsername = (EditText) findViewById(R.id.TextUsername);
                mTextUsername.setVisibility(View.GONE);
                mTextPassword = (EditText) findViewById(R.id.TextPassword);
                mTextPassword.setVisibility(View.GONE);
                mSpinnerSAKTZ = (Spinner) findViewById(R.id.SpinnerSAKTZ);
                mSpinnerSAKTZ.setVisibility(View.GONE);
                mSpinnerSyncInterval = (Spinner) findViewById(R.id.sync_interval);
                mSpinnerSyncInterval.setVisibility(View.GONE);
                mSpinnerCalendar = (Spinner) findViewById(R.id.calendar);
                mSpinnerCalendar.setVisibility(View.GONE);
                mSaveButton = (Button) findViewById(R.id.SaveButton);
                mSaveButton.setVisibility(View.GONE);


                //update values
                mUrlText = (TextView) findViewById(R.id.UrlText);
                mUrlText.setText(getResources().getString(R.string.sakai_url) + url);
                mUsernameText = (TextView) findViewById(R.id.UsernameText);
                mUsernameText.setText(getResources().getString(R.string.username) + username);
                mPasswordText = (TextView) findViewById(R.id.PasswordText);
                mPasswordText.setText(getResources().getString(R.string.password) + password);
                mCalIdText = (TextView) findViewById(R.id.CalIdText);
                mCalIdText.setText(getResources().getString(R.string.calendar) + calendarName);
                mSAKTZText = (TextView) findViewById(R.id.SAKTZText);
                mCalIdText.setText(getResources().getString(R.string.timezone) + timezone);

                mSyncIntervalText = (TextView) findViewById(R.id.SyncIntervalText);
                mSyncIntervalText.setText(getResources().getString(R.string.sync_interval) + syncInterval +"Hours");


                initIntent();
            } else {
                mTextSignIn.setVisibility(View.VISIBLE);
                //hide text fields
                mUrlText = (TextView) findViewById(R.id.UrlText);
                mUrlText.setVisibility(View.GONE);
                mUsernameText = (TextView) findViewById(R.id.UsernameText);
                mUsernameText.setVisibility(View.GONE);
                mPasswordText = (TextView) findViewById(R.id.PasswordText);
                mPasswordText.setVisibility(View.GONE);
                mPasswordText = (TextView) findViewById(R.id.PasswordText);
                mPasswordText.setVisibility(View.GONE);

                mSAKTZText = (TextView) findViewById(R.id.SAKTZText);
                mSAKTZText.setVisibility(View.GONE);


                mCalIdText = (TextView) findViewById(R.id.CalIdText);
                mCalIdText.setVisibility(View.GONE);
                mSyncIntervalText = (TextView) findViewById(R.id.SyncIntervalText);
                mSyncIntervalText.setVisibility(View.GONE);
                mEditButton = (Button) findViewById(R.id.EditButton);
                mEditButton.setVisibility(View.GONE);


                mSpinnerSyncInterval = (Spinner) findViewById(R.id.sync_interval);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.sync_intervals, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                mSpinnerSyncInterval.setAdapter(adapter);
                if(syncInterval!=0){
                    mSpinnerSyncInterval.setSelection(adapter.getPosition(syncInterval+""));
                }

                mSpinnerCalendar = (Spinner) findViewById(R.id.calendar);
                //calendar
                List<CalendarModel> calendars = EventUtil.readCalendars(getApplicationContext(), getApplicationContext().getContentResolver());

                ArrayAdapter<CalendarModel> adapter1 = new ArrayAdapter<CalendarModel>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, calendars);
                mSpinnerCalendar.setAdapter(adapter1);

                String name = mSettings.getString(Settings.PREF_LASTCALENDARNAME);
                int id = mSettings.getInt(Settings.PREF_LASTCALENDARID);
                if (id > 0 && name != null && !name.isEmpty()) {
                    CalendarModel cal = new CalendarModel(id, name);
                    mSpinnerCalendar.setSelection(adapter1.getPosition(cal));
                }

                mSpinnerSAKTZ = (Spinner) findViewById(R.id.SpinnerSAKTZ);
                ArrayAdapter<String> tzAdapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, TimeZone.getAvailableIDs());
                mSpinnerSAKTZ.setAdapter(tzAdapter);
                timezone = mSettings.getString(Settings.PREF_LASTSAKTZ);
                if (timezone  != null && !timezone .isEmpty()) {
                    mSpinnerSAKTZ.setSelection(tzAdapter.getPosition(timezone));
                }


            }
            Button button = (Button) findViewById(R.id.SaveButton);
            button.setOnClickListener(new View.OnClickListener()
                                      {
                                          private Activity getActivity(View view) {
                                              Context context = view.getContext();
                                              while (context instanceof ContextWrapper) {
                                                  if (context instanceof Activity) {
                                                      return (Activity) context;
                                                  }
                                                  context = ((ContextWrapper) context).getBaseContext();
                                              }
                                              return null;
                                          }

                                          @Override
                                          public void onClick(View view) {

                                              mTextCalendarUrl = (EditText) findViewById(R.id.TextCalendarUrl);
                                              String url = mTextCalendarUrl.getText().toString();
                                              mTextUsername = (EditText) findViewById(R.id.TextUsername);
                                              String username = mTextUsername.getText().toString();

                                              mTextPassword = (EditText) findViewById(R.id.TextPassword);
                                              String password = mTextPassword.getText().toString();

                                              mSpinnerSAKTZ = (Spinner) findViewById(R.id.SpinnerSAKTZ);
                                              String timezone = mSettings.getString(Settings.PREF_LASTSAKTZ);


                                              mSpinnerSyncInterval = (Spinner) findViewById(R.id.sync_interval);
                                              mTextCalendarUrl = (EditText) findViewById(R.id.TextCalendarUrl);
                                              mSpinnerCalendar = (Spinner) findViewById(R.id.calendar);

                                              String obj = (String)mSpinnerSyncInterval.getSelectedItem();

                                              //Log.i(TAG, "id:"+obj);
                                              long sync_interval =Integer.parseInt(obj);

                                              CalendarModel calendarModel = (CalendarModel) mSpinnerCalendar.getSelectedItem();
                                              int calID = calendarModel.getId();
                                              String calName = calendarModel.getName();
                                              boolean isValid = true;
                                              //Log.i(TAG, "Patterns.WEB_URL.matcher(url).matches():"+Patterns.WEB_URL.matcher(url).matches());
                                              if(url==null || url.isEmpty() || !(Patterns.WEB_URL.matcher(url).matches())){
                                                  mTextCalendarUrl.setError(getResources().getString(R.string.invalid_url));
                                                  isValid = false;
                                              }
                                              if(username==null || username.isEmpty()){
                                                  mTextUsername.setError(getResources().getString(R.string.invalid_username));
                                                  isValid = false;
                                              }
                                              if(password==null || password.isEmpty()){
                                                  mTextPassword.setError(getResources().getString(R.string.invalid_password));
                                                  isValid = false;
                                              }
                                              /*if(timezone==null || timezone.isEmpty()){
                                                  mSpinnerSAKTZ.setError(getResources().getString(R.string.invalid_password));
                                                  isValid = false;
                                              }*/
                                              Log.i(TAG, ""+isValid);
                                              if (isValid) {
                                                  new SendLoginJob().execute(url, username, password);
                                                  mProgress.setVisibility(View.VISIBLE);
                                                  mTextSignIn.setVisibility(View.VISIBLE);

                                              }




                                          }
                                      }

            );

            mEditButton = (Button)findViewById(R.id.EditButton);
            mEditButton.setOnClickListener(new View.OnClickListener()
                                           {
                                               private Activity getActivity(View view) {
                                                   Context context = view.getContext();
                                                   while (context instanceof ContextWrapper) {
                                                       if (context instanceof Activity) {
                                                           return (Activity) context;
                                                       }
                                                       context = ((ContextWrapper) context).getBaseContext();
                                                   }
                                                   return null;
                                               }

                                               @Override
                                               public void onClick(View view) {
                                                   mTextSignIn.setVisibility(View.VISIBLE);
                                                   mUrlText = (TextView) findViewById(R.id.UrlText);
                                                   mUrlText.setVisibility(View.GONE);
                                                   mUsernameText = (TextView) findViewById(R.id.UsernameText);
                                                   mUsernameText.setVisibility(View.GONE);
                                                   mPasswordText = (TextView) findViewById(R.id.PasswordText);
                                                   mPasswordText.setVisibility(View.GONE);
                                                   mSAKTZText = (TextView) findViewById(R.id.SAKTZText);
                                                   mSAKTZText.setVisibility(View.GONE);

                                                   mCalIdText = (TextView) findViewById(R.id.CalIdText);
                                                   mCalIdText.setVisibility(View.GONE);
                                                   mSyncIntervalText = (TextView) findViewById(R.id.SyncIntervalText);
                                                   mSyncIntervalText.setVisibility(View.GONE);
                                                   mEditButton = (Button) findViewById(R.id.EditButton);
                                                   mEditButton.setVisibility(View.GONE);

                                                   mTextCalendarUrl = (EditText) findViewById(R.id.TextCalendarUrl);
                                                   mTextCalendarUrl.setVisibility(View.VISIBLE);
                                                   mTextUsername = (EditText) findViewById(R.id.TextUsername);
                                                   mTextUsername.setVisibility(View.VISIBLE);
                                                   mTextPassword = (EditText) findViewById(R.id.TextPassword);
                                                   mTextPassword.setVisibility(View.VISIBLE);
                                                   mSpinnerSAKTZ = (Spinner) findViewById(R.id.SpinnerSAKTZ);
                                                   mSpinnerSAKTZ.setVisibility(View.VISIBLE);

                                                   //mSpinnerSyncInterval = (Spinner) findViewById(R.id.sync_interval);
                                                   mSpinnerSyncInterval.setVisibility(View.VISIBLE);
                                                   //mSpinnerCalendar = (Spinner) findViewById(R.id.calendar);
                                                   mSpinnerCalendar.setVisibility(View.VISIBLE);
                                                   mSaveButton = (Button) findViewById(R.id.SaveButton);
                                                   mSaveButton.setVisibility(View.VISIBLE);

                                                   mTextCalendarUrl.setText(mSettings.getString(Settings.PREF_LASTURL));
                                                   mTextUsername.setText(mSettings.getString(Settings.PREF_LASTURLUSERNAME));
                                                   mTextPassword.setText(mSettings.getString(Settings.PREF_LASTURLPASSWORD));

                                                   //sync intervals
                                                   // Create an ArrayAdapter using the string array and a default spinner layout
                                                   ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                                           R.array.sync_intervals, android.R.layout.simple_spinner_item);
                                                   // Specify the layout to use when the list of choices appears
                                                   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                   // Apply the adapter to the spinner
                                                   Log.i(TAG, "smSpinnerSyncInterval:" + mSpinnerSyncInterval);
                                                   mSpinnerSyncInterval.setAdapter(adapter);
                                                   int syncInterval = mSettings.getInt(Settings.PREF_LASTSYNCINTERVAL);
                                                   if(syncInterval!=0){
                                                       mSpinnerSyncInterval.setSelection(adapter.getPosition(syncInterval+""));
                                                   }

                                                   //mSpinnerCalendar = (Spinner) view.findViewById(R.id.calendar);
                                                   //calendar
                                                   List<CalendarModel> calendars = EventUtil.readCalendars(getApplicationContext(), getApplicationContext().getContentResolver());

                                                   ArrayAdapter<CalendarModel> adapter1 = new ArrayAdapter<CalendarModel>(getApplicationContext(), android.R.layout.simple_spinner_item, calendars);
                                                   mSpinnerCalendar.setAdapter(adapter1);

                                                   String name = mSettings.getString(Settings.PREF_LASTCALENDARNAME);
                                                   int id = mSettings.getInt(Settings.PREF_LASTCALENDARID);
                                                   if (id > 0 && name != null && !name.isEmpty()) {
                                                       CalendarModel cal = new CalendarModel(id, name);
                                                       mSpinnerCalendar.setSelection(adapter1.getPosition(cal));
                                                   }

                                                   mSpinnerSAKTZ = (Spinner) findViewById(R.id.SpinnerSAKTZ);
                                                   ArrayAdapter<String> tzAdapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, TimeZone.getAvailableIDs());
                                                   mSpinnerSAKTZ.setAdapter(tzAdapter);
                                                   String timezone = mSettings.getString(Settings.PREF_LASTSAKTZ);
                                                   if (timezone  != null && !timezone .isEmpty()) {
                                                       mSpinnerSAKTZ.setSelection(tzAdapter.getPosition(timezone));
                                                   }


                                               }
                                           }
            );
        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isGranted(final String permission) {
        final int status = ContextCompat.checkSelfPermission(this, permission);
        return status == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        boolean allGranted = true;
        for (final String permission : MY_PERMISSIONS)
            if (!isGranted(permission)) {
                allGranted = false;
                break;
            }

        if (allGranted)
            return true;

        ActivityCompat.requestPermissions(this, MY_PERMISSIONS, MY_PERMISSIONS_REQUEST);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                boolean allGranted = false;

                if (grantResults.length > 0) {
                    allGranted = true;
                    for (int grantResult : grantResults)
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            allGranted = false;
                            break;
                        }
                }

                if (!allGranted) {
                    Toast.makeText(this, R.string.permissions_not_granted, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
        }
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    public void initIntent() {
        Intent intent1 = new Intent(getApplicationContext(), AlarmReceiver.class);//the same as up
        intent1.setAction(AlarmReceiver.ACTION_ALARM);//the same as up
        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 1001,
                intent1, PendingIntent.FLAG_NO_CREATE) != null);
        Log.i(TAG, "alarmUp:"+alarmUp);
        if (!alarmUp) {

            startIntent();
        }

    }

    public void startIntent() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_ALARM);//my custom string action name
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent, PendingIntent.FLAG_CANCEL_CURRENT);//used unique ID as 1001
        int syncInterval = mSettings.getInt(Settings.PREF_LASTSYNCINTERVAL);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * syncInterval , pendingIntent);//first start will start asap

        //checking if alram is working with pendingIntent
        Intent intent1 = new Intent(getApplicationContext(), AlarmReceiver.class);//the same as up
        intent1.setAction(AlarmReceiver.ACTION_ALARM);//the same as up
        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 1001,
                intent1, PendingIntent.FLAG_NO_CREATE) != null);
        boolean isWorking = (PendingIntent.getBroadcast(getApplicationContext(), 1001, intent1, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag

        Toast.makeText(getApplicationContext(), "syncing events starting", Toast.LENGTH_SHORT).show();
        Intent eventSync = new Intent(getApplicationContext(), EventsSyncService.class);
        getApplicationContext().startService(eventSync);
        Log.d(TAG, "alarm is " + (isWorking ? "" : "not") + " working..." +"\t alarmUp"+alarmUp);

    }

    public Settings getSettings() {
        return mSettings;
    }

    public boolean isDataAvailable(String url, String username, String password, int calID, int syncInterval, String timezone) {
        return (!(url == null || url.isEmpty() || username == null || username.isEmpty() ||
                password == null || password.isEmpty() || calID == 0 || syncInterval == 0 || timezone == null || timezone.isEmpty()));
    }
    private class SendLoginJob extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here
            //check authenticate

            boolean isAuthenticated = false;
            try{
                isAuthenticated =  SakaiUtil.authenticate(params[0], params[1], params[2]);
                if(isAuthenticated){
                   return "success";
                }
            }catch (IOException e){
                //
            }


            return "false";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
            Log.i(TAG,message );
            if("success".equals(message)){
                mProgress.setVisibility(View.INVISIBLE);
                mTextCalendarUrl = (EditText) findViewById(R.id.TextCalendarUrl);
                String url = mTextCalendarUrl.getText().toString();
                mTextUsername = (EditText) findViewById(R.id.TextUsername);
                String username = mTextUsername.getText().toString();

                mTextPassword = (EditText) findViewById(R.id.TextPassword);
                String password = mTextPassword.getText().toString();
                mSpinnerSAKTZ = (Spinner) findViewById(R.id.SpinnerSAKTZ);
                String timezone = (String)mSpinnerSAKTZ.getSelectedItem();


                mSpinnerSyncInterval = (Spinner) findViewById(R.id.sync_interval);
                mTextCalendarUrl = (EditText) findViewById(R.id.TextCalendarUrl);
                mSpinnerCalendar = (Spinner) findViewById(R.id.calendar);

                String obj = (String)mSpinnerSyncInterval.getSelectedItem();

                Log.i(TAG, "id:"+obj);
                long sync_interval =Integer.parseInt(obj);

                CalendarModel calendarModel = (CalendarModel) mSpinnerCalendar.getSelectedItem();
                int calID = calendarModel.getId();
                String calName = calendarModel.getName();

                //mTextPassword.setError(getResources().getString(R.string.invalid_credentials));
                //cancel existing background service
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra(AlarmReceiver.ACTION_ALARM, AlarmReceiver.ACTION_ALARM);
                final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarms.cancel(pIntent);

                //save new settings

                mSettings.putString(Settings.PREF_LASTURL, url);

                mSettings.putString(Settings.PREF_LASTURLUSERNAME, username);
                mSettings.putString(Settings.PREF_LASTURLPASSWORD, password);
                mSettings.putString(Settings.PREF_LASTSAKTZ, timezone);
                mSettings.putString(Settings.PREF_LASTCALENDARNAME, calName);
                mSettings.putInt(Settings.PREF_LASTCALENDARID, calID);
                mSettings.putInt(Settings.PREF_LASTSYNCINTERVAL, (int) sync_interval);

                mTextCalendarUrl = (EditText) findViewById(R.id.TextCalendarUrl);
                mTextCalendarUrl.setVisibility(View.GONE);
                mTextUsername = (EditText) findViewById(R.id.TextUsername);
                mTextUsername.setVisibility(View.GONE);
                mTextPassword = (EditText) findViewById(R.id.TextPassword);
                mTextPassword.setVisibility(View.GONE);
                mSpinnerSAKTZ = (Spinner) findViewById(R.id.SpinnerSAKTZ);
                mSpinnerSAKTZ.setVisibility(View.GONE);
                mSpinnerSyncInterval = (Spinner) findViewById(R.id.sync_interval);
                mSpinnerSyncInterval.setVisibility(View.GONE);
                mSpinnerCalendar = (Spinner) findViewById(R.id.calendar);
                mSpinnerCalendar.setVisibility(View.GONE);
                mSaveButton = (Button) findViewById(R.id.SaveButton);
                mSaveButton.setVisibility(View.GONE);

                mUrlText = (TextView) findViewById(R.id.UrlText);
                mUrlText.setText(getResources().getString(R.string.sakai_url) + url);
                mUrlText.setVisibility(View.VISIBLE);

                mUsernameText = (TextView) findViewById(R.id.UsernameText);
                mUsernameText.setText(getResources().getString(R.string.username) + username);
                mUsernameText.setVisibility(View.VISIBLE);

                mPasswordText = (TextView) findViewById(R.id.PasswordText);
                mPasswordText.setText( getResources().getString(R.string.password) + password);
                mPasswordText.setVisibility(View.VISIBLE);

                mCalIdText = (TextView) findViewById(R.id.CalIdText);
                mCalIdText.setText(getResources().getString(R.string.calendar) + calName);
                mCalIdText.setVisibility(View.VISIBLE);
                mSAKTZText = (TextView) findViewById(R.id.SAKTZText);
                mSAKTZText.setText(getResources().getString(R.string.timezone) + timezone);
                mSAKTZText.setVisibility(View.VISIBLE);
                mSyncIntervalText = (TextView) findViewById(R.id.SyncIntervalText);
                mSyncIntervalText.setText(getResources().getString(R.string.sync_interval) + sync_interval+"Hours");
                mSyncIntervalText.setVisibility(View.VISIBLE);

                mEditButton = (Button) findViewById(R.id.EditButton);
                mEditButton.setVisibility(View.VISIBLE);
                mTextSignIn.setVisibility(View.GONE);
                startIntent();
            }else{
                mProgress.setVisibility(View.VISIBLE);
                mTextSignIn.setVisibility(View.VISIBLE);
                mTextPassword.setError(getResources().getString(R.string.invalid_credentials));
            }
        }
    }
}
