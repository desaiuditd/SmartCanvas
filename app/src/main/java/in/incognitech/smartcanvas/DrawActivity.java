package in.incognitech.smartcanvas;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.incognitech.smartcanvas.db.NoteDbHelper;
import in.incognitech.smartcanvas.views.PaintView;

public class DrawActivity extends AppCompatActivity {

    private PaintView paintView;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            DrawActivity.this.resetCanvas();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        paintView = (PaintView) findViewById(R.id.paint);

        FloatingActionButton list = (FloatingActionButton) findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DrawActivity.this, GridActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton save = (FloatingActionButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.setDrawingCacheEnabled(true);
                paintView.buildDrawingCache();
                Bitmap image = paintView.getDrawingCache();
                System.out.println(image);
                try {
                    File file = createImageFile();
                    System.out.println(file);
                    FileOutputStream ostream = new FileOutputStream(file);
                    image.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                    ostream.close();
                    paintView.invalidate();
                    paintView.clear();

                    SQLiteDatabase db = new NoteDbHelper(DrawActivity.this).getWritableDatabase();
                    ContentValues newValues = new ContentValues();
                    newValues.put(NoteDbHelper.PHOTO_PATH_COLUMN, file.getAbsolutePath());
                    db.insert(NoteDbHelper.DATABASE_TABLE, null, newValues);

                    Toast.makeText(getApplicationContext(), "Note saved successfully!", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                paintView.setDrawingCacheEnabled(false);
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

    }

    private void resetCanvas() {
        int accel = Math.abs((int) mAccel);
        if (accel > 5) {
            System.out.println(accel);
            paintView.clear();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

}
