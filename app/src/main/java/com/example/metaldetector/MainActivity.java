package com.example.metaldetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor magnetSensor;

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

        mLogConfigurator .setFileName( fileName );
        mLogConfigurator .setMaxFileSize( maxFileSize );
        mLogConfigurator .setFilePattern(filePattern);
        mLogConfigurator .setMaxBackupSize(maxBackupSize);
        mLogConfigurator .setUseLogCatAppender(true);
        mLogConfigurator .setUseFileAppender(true);
        mLogConfigurator .configure();

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
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
