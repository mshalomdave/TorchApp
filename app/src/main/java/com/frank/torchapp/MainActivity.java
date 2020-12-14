package com.frank.torchapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private CameraManager camManager;
    private String camId;

    private ToggleButton switchBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check if flash is on the device
        boolean isFlashThere = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashThere) {
            //If not show error
            showFlashError();
        }

        //initialize or get the camera manager and camera id
        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            camId = camManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
//            e.printStackTrace();
            Toast.makeText(this, "Error Setting up Torch", Toast.LENGTH_SHORT).show();
        }

        //Set up the toggle button
        switchBtn = findViewById(R.id.switchBtn);

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //On click, switch on or off the torch flash
                switchFlashLight(isChecked);
            }
        });
    }

    //  This method is to display an error in case there is no flash available on the device
    public void showFlashError() {
        AlertDialog errorDialog = new AlertDialog.Builder(this)
                .create();
        errorDialog.setTitle("Error!");
        errorDialog.setMessage("Flash unavailable on this device...");
        errorDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        errorDialog.show();
    }

    //The method below is to switch on and off the Torch
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void switchFlashLight(boolean status) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                camManager.setTorchMode(camId, status);
            }
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error Switching on Torch", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
        }
    }
}