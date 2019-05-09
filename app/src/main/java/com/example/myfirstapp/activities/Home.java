package com.example.myfirstapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Utils.Utils;
import com.example.myfirstapp.services.ShakerService;
import io.fabric.sdk.android.Fabric;

public class Home extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;

    private Button mBtnGraph, mBtnElectriv, mBtnLines;
    private int isCall=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        mContext = this;
        initView();
        initListners();
        Intent startIntent = new Intent(mContext, ShakerService.class);
        startService(startIntent);
    }

    private void initView() {
        mBtnGraph = findViewById(R.id.btn_graph);
        mBtnElectriv = findViewById(R.id.btn_electric);
        mBtnLines = findViewById(R.id.btn_lines);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initListners() {
        mBtnGraph.setOnClickListener(this);
        mBtnElectriv.setOnClickListener(this);
        mBtnLines.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_graph:
                isCall=1;
                if (Utils.checkCameraStoragePermission(mContext)) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.btn_electric:
                isCall=2;
                if (Utils.checkCameraStoragePermission(mContext)) {
                    openPopupBar(true);
                }
                break;

            case R.id.btn_lines:
                isCall=3;
                if (Utils.checkCameraStoragePermission(mContext)) {
                    openPopupBar(false);
                }
                break;
        }

    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void openPopupBar(final boolean isElectrical) {
        final Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_edit_value_range);
        ImageView mImgCancel = mDialog.findViewById(R.id.img_cancel);
        TextView mTvMain = mDialog.findViewById(R.id.tv_main);
        TextView mTvSub1 = mDialog.findViewById(R.id.tv_sub1);
        TextView mTvSub2 = mDialog.findViewById(R.id.tv_sub2);
        TextView mTvSub3 = mDialog.findViewById(R.id.tv_sub3);
        final EditText mEtValueMin = mDialog.findViewById(R.id.et_minvalue);
        final EditText mEtValueMax = mDialog.findViewById(R.id.et_maxvalue);
        final EditText mEtValueMin1 = mDialog.findViewById(R.id.et_minvalue1);
        final EditText mEtValueMax1 = mDialog.findViewById(R.id.et_maxvalue1);
        final EditText mEtValueMin2 = mDialog.findViewById(R.id.et_minvalue2);
        final EditText mEtValueMax2 = mDialog.findViewById(R.id.et_maxvalue2);
        if (isElectrical) {
            mTvMain.setText(R.string.electronic_metal);
            mTvSub1.setText(R.string.closed_metals);
            mTvSub2.setText(R.string.light_device);
            mTvSub3.setText(R.string.heavy_device);
            mEtValueMin.setText("80");
            mEtValueMax.setText("99");
            mEtValueMin1.setText("100");
            mEtValueMax1.setText("199");
            mEtValueMin2.setText("200");
            mEtValueMax2.setText("1000");
        } else {
            mTvMain.setText(R.string.buried_lines);
            mTvSub1.setText(R.string.light_wiring);
            mTvSub2.setText(R.string.medium_wiring);
            mTvSub3.setText(R.string.heavy_wirig);
            mEtValueMin.setText("70");
            mEtValueMax.setText("89");
            mEtValueMin1.setText("90");
            mEtValueMax1.setText("169");
            mEtValueMin2.setText("170");
            mEtValueMax2.setText("1000");
        }
        Button mBtnCamera = mDialog.findViewById(R.id.btn_ok);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setAttributes(lp);
        mImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEtValueMin.getText().toString())) {
                    Toast.makeText(mContext, "Please enter the comparison value", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEtValueMax.getText().toString())) {
                    Toast.makeText(mContext, "Please enter the comparison value", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEtValueMin1.getText().toString())) {
                    Toast.makeText(mContext, "Please enter the comparison value", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEtValueMax1.getText().toString())) {
                    Toast.makeText(mContext, "Please enter the comparison value", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEtValueMin2.getText().toString())) {
                    Toast.makeText(mContext, "Please enter the comparison value", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEtValueMax2.getText().toString())) {
                    Toast.makeText(mContext, "Please enter the comparison value", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, DeviceCategory.class);
                    if (isElectrical) {
                        intent.putExtra("case", "electrical");
                    }
                    intent.putExtra("min", mEtValueMin.getText().toString());
                    intent.putExtra("max", mEtValueMax.getText().toString());
                    intent.putExtra("min1", mEtValueMin1.getText().toString());
                    intent.putExtra("max1", mEtValueMax1.getText().toString());
                    intent.putExtra("min2", mEtValueMin2.getText().toString());
                    intent.putExtra("max2", mEtValueMax2.getText().toString());
                    startActivity(intent);
                    mDialog.dismiss();
                }

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1213:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Utils.checkCameraStoragePermission(mContext)) {
                       if(isCall==1){
                           Intent intent = new Intent(mContext, MainActivity.class);
                           startActivity(intent);
                       } else if(isCall==2){
                           openPopupBar(true);
                       } else {
                           openPopupBar(false);
                       }
                    } else {
                        Toast.makeText(mContext, R.string.please_grant_camera_storage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.please_grant_camera_storage_manually, Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

}
