package com.project.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String TAG = "SelfieApp.MainActivity";
    private static final String APP_FOLDER = "DailySelfie";
    protected static final String EXTRA_RES_IMG = "IMG";

    private ListView listView;
    private ArrayList<SelfieImage> filesInFolder;
    private Uri fileUri;

    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent, mLoggerReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent,
            mLoggerReceiverPendingIntent;

    private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
    protected static final long JITTER = 5000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Intent to broadcast to the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(MainActivity.this,
                NotificationReceiver.class);

        // PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mNotificationReceiverIntent, 0);

        // Intent to broadcast to the AlarmLoggerReceiver
        mLoggerReceiverIntent = new Intent(MainActivity.this,
                AlarmReceiver.class);

        // PendingIntent that holds the mLoggerReceiverPendingIntent
        mLoggerReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mLoggerReceiverIntent, 0);

        // Set inexact repeating alarm
        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                INITIAL_ALARM_DELAY,
                mNotificationReceiverPendingIntent);
        // Set inexact repeating alarm to fire shortly after previous alarm
        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY
                        + JITTER,
                INITIAL_ALARM_DELAY,
                mLoggerReceiverPendingIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        filesInFolder = getFiles(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + File.separator + APP_FOLDER);
        if (filesInFolder != null) {
            listView.setAdapter(new SelfieImageAdapter(this, filesInFolder.toArray(new SelfieImage[filesInFolder.size()])));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Intent to start the ImageViewActivity
                    Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);

                    // Add the raw bytes of the image to display as an Intent Extra
                    intent.putExtra(EXTRA_RES_IMG, filesInFolder.get(position).getRawImage());

                    // Start the ImageViewActivity
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_camera) {
            // maybe a bit insecure
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // create a file to save the image
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            } else {
                Toast.makeText(this, "No external storage found", Toast.LENGTH_LONG).show();
            }

            Intent intent = null;

            if (fileUri != null){
                // create Intent to take a picture and return control to the calling application
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // set the image file name
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            } else {
                Log.d(TAG, "An error occurred during creating the picture file.");
                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<SelfieImage> getFiles(String directoryPath) {
        ArrayList<SelfieImage> res = new ArrayList<>();
        File file = new File(directoryPath);

        file.mkdirs();
        File[] files = file.listFiles();
        if (files == null || files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++) {
                res.add(new SelfieImage(getApplicationContext(), files[i]));
            }
        }

        return res;
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_FOLDER);
        // Location for shareable file between applications (persist after uninstalling the app)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            String path = mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg";
            mediaFile = new File(path);
        } else {
            return null;
        }

        return mediaFile;
    }

    public class SelfieImageAdapter extends ArrayAdapter<SelfieImage> {
        private final Context context;
        private final SelfieImage[] values;
        private final LayoutInflater inflater;

        public SelfieImageAdapter(Context context, SelfieImage[] values) {
            super(context, R.layout.list_row, values);
            this.context = context;
            this.values = values;
            this.inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = inflater.inflate(R.layout.list_row, parent, false);

            TextView title = (TextView) rowView.findViewById(R.id.firstLine);
            title.setText(values[position].getTitle());

            ImageView thumb = (ImageView) rowView.findViewById(R.id.icon);
            thumb.setImageDrawable(values[position].getThumbImage());

            TextView description = (TextView) rowView.findViewById(R.id.secondLine);
            description.setText(values[position].getDescription());

            return rowView;
        }
    }

}
