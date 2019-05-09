package com.example.metaldetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor magnetSensor;

    private float x_arr[];
    private float y_arr[];
    private float z_arr[];

    private float rsum_x;
    private float rsum_y;
    private float rsum_z;

    private float min_x;
    private float min_y;
    private float min_z;

    private float max_x;
    private float max_y;
    private float max_z;

    private int index;

    private Logger logger;

    private void configureLog4j() {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "log4j.log";
        String filePattern = "%d - [%c] - %p : %m%n";
        int maxBackupSize = 10;
        long maxFileSize = 1024 * 1024;

        configure( fileName, filePattern, maxBackupSize, maxFileSize );
    }

    private void configure( String fileName, String filePattern, int maxBackupSize, long maxFileSize ) {
        LogConfigurator mLogConfigurator = new LogConfigurator();

        mLogConfigurator.setFileName( fileName );
        mLogConfigurator.setMaxFileSize( maxFileSize );
        mLogConfigurator.setFilePattern(filePattern);
        mLogConfigurator.setMaxBackupSize(maxBackupSize);
        mLogConfigurator.setUseLogCatAppender(true);
        mLogConfigurator.setUseFileAppender(true);
        mLogConfigurator.configure();

    }

    public Logger getLogger( String name ) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( name );
        return logger;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureLog4j();
        logger = getLogger("MainActivity");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetSensor = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == magnetSensor) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double metalpower = Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));

            Toast.makeText(getApplicationContext(),"MetalPower: " + metalpower, Toast.LENGTH_SHORT).show();
            String str = new String("X: " + x + " Y: " + y + " Z: " + z + " Strength: " + metalpower);
            logger.info(str);

            TextView textView1 = (TextView)findViewById(R.id.locationTextView1);
            TextView textView2 = (TextView)findViewById(R.id.locationTextView2);
            TextView textView3 = (TextView)findViewById(R.id.locationTextView3);
            TextView textView4 = (TextView)findViewById(R.id.locationTextView4);

            textView1.setText("X: " + x);
            textView2.setText("Y: " + y);
            textView3.setText("Z: " + z);
            textView4.setText("Strength: " + metalpower);

            //+
            TextView textView5 = (TextView)findViewById(R.id.locationTextView5);
            TextView textView6 = (TextView)findViewById(R.id.locationTextView6);
            TextView textView7 = (TextView)findViewById(R.id.locationTextView7);
            TextView textView8 = (TextView)findViewById(R.id.locationTextView8);
            //-

            rsum_x = rsum_x - x_arr[index] + x;
            rsum_y = rsum_y - y_arr[index] + y;
            rsum_z = rsum_z - z_arr[index] + z;

            x_arr[index] = x;
            y_arr[index] = y;
            z_arr[index] = z;


            if (9 == index) {
                index = 0;
            } else {
                index++;
            }

            if (min_x > x) {
                min_x = x;
            }
            if (min_y > y) {
                min_y = y;
            }
            if (min_z > z) {
                min_z = z;
            }

            if (max_x > x) {
                max_x = x;
            }
            if (max_y > y) {
                max_y = y;
            }
            if (max_z > z) {
                max_z = z;
            }

            float avg_delta_x = (max_x - min_x) / 2;
            float avg_delta_y = (max_y - min_y) / 2;
            float avg_delta_z = (max_z - min_z) / 2;

            float avg_delta = (avg_delta_x + avg_delta_y + avg_delta_z) / 3;

            float scale_x = 1.0f;
            if (0 != avg_delta_x) {
                scale_x = avg_delta / avg_delta_x;
            }
            float scale_y = 1.0f;
            if (0 != avg_delta_y) {
                scale_y = avg_delta / avg_delta_y;
            }
            float scale_z = 1.0f;
            if (0 != avg_delta_z) {
                scale_z = avg_delta / avg_delta_z;
            }

            float corrected_x = x * scale_x;
            float corrected_y = y * scale_y;
            float corrected_z = z * scale_z;
            float corrected_metalpower = Math.round(Math.sqrt(Math.pow(corrected_x, 2)
                    + Math.pow(corrected_y, 2)
                    + Math.pow(corrected_z, 2)));

            textView5.setText("Corr X: " + corrected_x);
            textView6.setText("Corr Y: " + corrected_y);
            textView7.setText("Corr Z: " + corrected_z);
            textView8.setText("Corr Strength: " + corrected_metalpower);

            str = new String("Corr X: "
                    + corrected_x + " Corr Y: "
                    + corrected_y + " Corr Z: "
                    + corrected_z + " Corr Strength: "
                    + corrected_metalpower);
            logger.info(str);
            //-

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
