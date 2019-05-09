package com.example.myfirstapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Utils.LineCharts;
import com.example.myfirstapp.Utils.Log4jHelper;

import java.math.BigDecimal;
import java.util.Arrays;

import lecho.lib.hellocharts.view.LineChartView;

public class DeviceCategory extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private Context mContext;
    private MediaPlayer mPlayer;
    private TextView tvStatus;
    private TextView mTvTitle;
    private int min,max,min1,max1,min2,max2;
    private boolean isElecrtical=false;
    private ImageView mImgBack;
   // private TextView mTvEdit;
    private SensorManager sensorManager;
    private Sensor magnetSensor;
    private org.apache.log4j.Logger logger;
    private float x_arr[];
    private float y_arr[];
    private float z_arr[];
    int index;
    float rsum_x;
    float rsum_y;
    float rsum_z;
    float min_x;
    float min_y;
    float min_z;
    float max_x;
    float max_y;
    float max_z;
    private TextView tvX;
    private TextView tvY;
    private TextView tvZ;
    private TextView tvStrength;
    private LineChartView lineChart;
    private LineCharts lineCharts = new LineCharts();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_category);
        mContext=this;
        initView();
        initListners();
        getIntentData();
        initSensorReadings();
    }

    private void getIntentData() {
        if(getIntent().getExtras().containsKey("case")) {
            isElecrtical = true;
            mTvTitle.setText(R.string.electronic_metal);
        }else {
            mTvTitle.setText(R.string.buried_lines);
        }
            min=Integer.valueOf(getIntent().getStringExtra("min"));
            max=Integer.valueOf(getIntent().getStringExtra("max"));
            min1=Integer.valueOf(getIntent().getStringExtra("min1"));
            max1=Integer.valueOf(getIntent().getStringExtra("max1"));
            min2=Integer.valueOf(getIntent().getStringExtra("min2"));
            max2=Integer.valueOf(getIntent().getStringExtra("max2"));

    }

    private void initView() {
        tvX = findViewById(R.id.tv_x);
        tvY = findViewById(R.id.tv_y);
        tvZ = findViewById(R.id.tv_z);
        tvStrength = findViewById(R.id.tv_strength);
        mImgBack=findViewById(R.id.img_back);
        lineChart = findViewById(R.id.chart);
        lineCharts.initView(lineChart);
       // mTvEdit=findViewById(R.id.tv_edit);
        tvStatus = findViewById(R.id.tv_status);
        mTvTitle=findViewById(R.id.tv_title);
    }

    private void initListners() {
        mImgBack.setOnClickListener(this);
       // mTvEdit.setOnClickListener(this);
    }

    private void initSensorReadings() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetSensor = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log4jHelper log4jh = new Log4jHelper(getApplicationContext());
        logger = log4jh.getLogger("MainActivity");
        x_arr = new float[10];
        Arrays.fill(x_arr, 0);
        y_arr = new float[10];
        Arrays.fill(y_arr, 0);
        z_arr = new float[10];
        Arrays.fill(z_arr, 0);
        rsum_x = 0.0f;
        rsum_y = 0.0f;
        rsum_z = 0.0f;
        index = 0;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.img_back:
                finish();
                break;

//            case R.id.tv_edit:
//               // openPopupBar();
//                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double metalpower = Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));

        String str = new String("X: " + x + " Y: " + y + " Z: " + z + " Strength: " + metalpower);
        logger.info(str);

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

        if(isElecrtical){
             if(corrected_metalpower>=min && corrected_metalpower<=max){
                tvStatus.setText("Unknown device with enclosed metal present");
             }else if(corrected_metalpower>=min1 && corrected_metalpower<=max1){
                 if(mPlayer!=null){
                     mPlayer.release();
                 }
                 mPlayer= MediaPlayer.create(this, R.raw.beep);
                 mPlayer.start();
                 tvStatus.setText("Electronic device with low EMF present (e.g TV, PlayStation, Speakers)");
             }else if(corrected_metalpower>=min2 && corrected_metalpower<=max2){
                 if(mPlayer!=null){
                     mPlayer.release();
                 }
                 mPlayer= MediaPlayer.create(this, R.raw.beephigh);
                 mPlayer.start();
                 tvStatus.setText("Electronic device with high EMF present e.g Laptop, Desktop");
             }else {
                 tvStatus.setText("");
             }
        }else {
             if(corrected_metalpower>=min && corrected_metalpower<=max){
                 tvStatus.setText("Light presence of buried power lines");
             }else if(corrected_metalpower>=min1 && corrected_metalpower<=max1){
                 if(mPlayer!=null){
                     mPlayer.release();
                 }
                 mPlayer= MediaPlayer.create(this, R.raw.beep);
                 mPlayer.start();
                 tvStatus.setText("Moderate presence of buried power lines");
             }else if(corrected_metalpower>=min2 && corrected_metalpower<=max2){
                 if(mPlayer!=null){
                     mPlayer.release();
                 }
                 mPlayer= MediaPlayer.create(this, R.raw.beephigh);
                 mPlayer.start();
                 tvStatus.setText("Heavy presence of buried power lines");
             }else {
                 tvStatus.setText("");
             }
         }

        tvX.setText("Corr X: " + corrected_x);
        tvY.setText("Corr Y: " + corrected_y);
        tvZ.setText("Corr Z: " + corrected_z);
        tvStrength.setText("Corr Strength: " + corrected_metalpower + " μT");

        str = new String("Corr X: "
                + corrected_x + " Corr Y: "
                + corrected_y + " Corr Z: "
                + corrected_z + " Corr Strength: "
                + corrected_metalpower);
        logger.info(str);

        BigDecimal total = new BigDecimal(corrected_metalpower);
        double res = total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        lineCharts.makeCharts(lineChart, (float) res);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);

        if (null != mPlayer) {
            mPlayer.release();
        }
    }
}
