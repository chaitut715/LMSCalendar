/**
 * Copyright (C) 2015  Jon Griffiths (jon_p_griffiths@yahoo.com)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.chaitu.lmscalendar.ui;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaitu.lmscalendar.MainActivity;
import com.chaitu.lmscalendar.R;
import com.chaitu.lmscalendar.sakai.sync.AlarmReceiver;
import com.chaitu.lmscalendar.sakai.util.EventUtil;
import com.chaitu.lmscalendar.settings.Settings;

import java.util.List;

public class UrlDialog extends DialogFragment {

    private MainActivity mActivity;
    private EditText mTextCalendarUrl;
    private EditText mTextUsername;
    private EditText mTextPassword;
    private Spinner mSpinnerSyncInterval;
    private Spinner mSpinnerCalendar;

    public UrlDialog() {
    }

    public static void show(final Activity activity) {
        FragmentManager fm = ((MainActivity) activity).getSupportFragmentManager();
        new UrlDialog().show(fm, "UrlDialogTag");
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();

        ViewGroup nullViewGroup = null; // Avoid bad lint warning in inflate()
        View view = mActivity.getLayoutInflater().inflate(R.layout.urldialog, nullViewGroup);


        mTextCalendarUrl = (EditText) view.findViewById(R.id.TextCalendarUrl);
        mTextUsername = (EditText) view.findViewById(R.id.TextUsername);
        mTextPassword = (EditText) view.findViewById(R.id.TextPassword);

        Settings settings = mActivity.getSettings();
        mTextCalendarUrl.setText(settings.getString(Settings.PREF_LASTURL));
        mTextUsername.setText(settings.getString(Settings.PREF_LASTURLUSERNAME));
        mTextPassword.setText(settings.getString(Settings.PREF_LASTURLPASSWORD));

        mTextCalendarUrl.selectAll();



        //sync intervals
        mSpinnerSyncInterval = (Spinner) view.findViewById(R.id.sync_interval);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity.getApplicationContext(),
                        R.array.sync_intervals, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerSyncInterval.setAdapter(adapter);

        mSpinnerCalendar = (Spinner) view.findViewById(R.id.calendar);
        //calendar
        List<CalendarModel> calendars = EventUtil.readCalendars(mActivity.getApplicationContext(), mActivity.getApplicationContext().getContentResolver());

        ArrayAdapter<CalendarModel> adapter1 = new ArrayAdapter<CalendarModel>(mActivity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, calendars);
        mSpinnerCalendar.setAdapter(adapter1);

        String name = settings.getString(Settings.PREF_LASTCALENDARNAME);
        int id = settings.getInt(Settings.PREF_LASTCALENDARID);
        if(id>0 && name!=null && !name.isEmpty() ){
            CalendarModel cal = new CalendarModel(id, name);
            mSpinnerCalendar.setSelection(adapter1.getPosition(cal));
        }

        DialogInterface.OnClickListener okTask;
        okTask = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface iface, int id) {
                // We override this in onStart()
            }
        };

        DialogInterface.OnClickListener cancelTask;
        cancelTask = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface iface, int id) {
                iface.cancel();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        AlertDialog dlg = builder.setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.enter_source_url)
                .setView(view)
                .setPositiveButton(android.R.string.ok, okTask)
                .setNegativeButton(android.R.string.cancel, cancelTask)
                .create();
        dlg.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dlg;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dlg = (AlertDialog) getDialog();
        if (dlg == null)
            return;

        View.OnClickListener onClickTask;
        onClickTask = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mTextCalendarUrl.getText().toString();
                String username = mTextUsername.getText().toString();
                String password = mTextPassword.getText().toString();
                String calendar = mTextPassword.getText().toString();
                long sync_interval = mSpinnerSyncInterval.getSelectedItemId();
                CalendarModel calendarModel = (CalendarModel) mSpinnerCalendar.getSelectedItem();
                int calID = calendarModel.getId();
                String calName = calendarModel.getName();

                /*if (!mActivity.setSource(url, null, username, password)) {
                    TextView label = (TextView) dlg.findViewById(R.id.TextViewUrlError);
                    label.setText(R.string.invalid_url);
                    return;
                }*/

                Intent intent = new Intent(getActivity(),AlarmReceiver.class);
                intent.putExtra(AlarmReceiver.ACTION_ALARM,AlarmReceiver.ACTION_ALARM);
                final PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(), 1234567,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarms = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarms.cancel(pIntent);

                Settings settings = mActivity.getSettings();
                settings.putString(Settings.PREF_LASTURL, url);

                settings.putString(Settings.PREF_LASTURLUSERNAME, username);
                settings.putString(Settings.PREF_LASTURLPASSWORD, password);
                settings.putString(Settings.PREF_LASTCALENDARNAME, calName);
                settings.putInt(Settings.PREF_LASTCALENDARID,calID );
                settings.putInt(Settings.PREF_LASTSYNCINTERVAL,(int)sync_interval );
                mActivity.startIntent();
                dlg.dismiss();

            }
        };

        dlg.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(onClickTask);

    }
}
