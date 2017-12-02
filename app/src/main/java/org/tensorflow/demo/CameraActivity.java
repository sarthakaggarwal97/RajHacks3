/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*  This file has been modified by Nataniel Ruiz affiliated with Wall Lab
 *  at the Georgia Institute of Technology School of Interactive Computing
 */

package org.tensorflow.demo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity {
  private static final int PERMISSIONS_REQUEST = 1;

  TextView t1;

  private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
  private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    setContentView(R.layout.activity_camera);


    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    SensorEventListener sensorEventListener = new SensorEventListener() {
      @Override
      public void onSensorChanged(SensorEvent event) {
        float y = event.values[1];
        y = (10 - ((y+10)/2)) * 18;
        int alpha = (int) y;
          t1 = (TextView) findViewById(R.id.distance);
            double distance = Math.tan(alpha)*10;
             String a =Double.toString(Math.abs(distance));
         t1.setText(a);
        if(distance<5){

          Toast.makeText(CameraActivity.this, "", Toast.LENGTH_SHORT).show();
          Toast.makeText(CameraActivity.this, "", Toast.LENGTH_SHORT).show();
          Toast.makeText(CameraActivity.this, "", Toast.LENGTH_SHORT).show();
          Toast.makeText(CameraActivity.this, "", Toast.LENGTH_SHORT).show();
          Toast.makeText(CameraActivity.this, "Slow Down Your Speed", Toast.LENGTH_SHORT).show();
//
//          AlertDialog.Builder builder;
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
//          } else {
//            builder = new AlertDialog.Builder(getApplicationContext());
//          }
//          builder.setTitle("\tCAUTION")
//                  .setMessage("Slow down your speed")
//                  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                      // continue with delete
//                    }
//                  })
//                  .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                      // do nothing
//                    }
//                  })
//                  .setIcon(android.R.drawable.ic_dialog_alert)
//                  .show();

        }
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {

      }
    };

    sensorManager.registerListener(sensorEventListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            100000);

    if (hasPermission()) {
      if (null == savedInstanceState) {
        setFragment();
      }
    } else {
      requestPermission();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case PERMISSIONS_REQUEST: {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          setFragment();
        } else {
          requestPermission();
        }
      }
    }
  }

  private boolean hasPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(PERMISSION_STORAGE) == PackageManager.PERMISSION_GRANTED;
    } else {
      return true;
    }
  }

  private void requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA) || shouldShowRequestPermissionRationale(PERMISSION_STORAGE)) {
        Toast.makeText(CameraActivity.this, "Camera AND storage permission are required for this demo", Toast.LENGTH_LONG).show();
      }
      requestPermissions(new String[] {PERMISSION_CAMERA, PERMISSION_STORAGE}, PERMISSIONS_REQUEST);
    }
  }

  private void setFragment() {
    getFragmentManager()
            .beginTransaction()
            .replace(R.id.container, CameraConnectionFragment.newInstance())
            .commit();
  }
}
