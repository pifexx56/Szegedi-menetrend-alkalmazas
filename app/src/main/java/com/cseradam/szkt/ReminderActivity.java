package com.cseradam.szkt;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText stopNameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        stopNameET = findViewById(R.id.stop_name_reminder);
        timePicker = findViewById(R.id.timePicker);
    }

    private void setReminder() {

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String stopName = stopNameET.getText().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.MINUTE, -5);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("stopName", stopName);
        editor.apply();

        // Create an Intent for the alarm receiver
        Intent intent = new Intent(this, AlarmReceiver.class);

        // Create a PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create a notification channel
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("reminder_channel", "Reminder Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Set the alarm
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Értesítés sikeresen beállítva!", Toast.LENGTH_SHORT).show();
        }
    }

    public void callSetReminder(View view)
    {
        if (stopNameET.getText().toString().isEmpty())
            stopNameET.setError("Kérlek add meg a megálló nevét.");
        else {
            setReminder();
        }
    }
    public void openNotificationSettings(View view) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
        }
        startActivity(intent);
    }

    public void callMainPage(View view)
    {
        startActivity(new Intent(this, MainPage.class));
    }
}
