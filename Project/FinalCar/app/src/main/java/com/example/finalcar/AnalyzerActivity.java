/* Copyright 2011 Google Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 * @author Stephen Uhler
 *
 * 2014 Eddy Xiao <bewantbe@gmail.com>
 * GUI extensively modified.
 * Add spectrogram plot, smooth gesture view control and various settings.
 */

package com.example.finalcar;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Formatter;

/**
 * Audio "FFT" analyzer.
 * @author suhler@google.com (Stephen Uhler)
 */

/* 5:18
 *
 * TODO
 *  1. Update fftActivity by Handler, msg from Sampling Loop
 *  2. Send btSend as var to fftActivity
 *  3. Find the specturm dBA array , and C,D,E freq and get dBA val
 *  4. Send Handler to AnalyzerActivity
 *  5. 合併
 *
 * TODO part
 *  1-1 use msg to update tv
 *
 * */

public class AnalyzerActivity extends Activity
{
    private static final String TAG="AnalyzerActivity:";

    SamplingLoop samplingThread = null;
    private AnalyzerParameters analyzerParam = null;

    double dtRMS = 0;
    double dtRMSFromFT = 0;
    double maxAmpDB;
    double maxAmpFreq;
    /*my self*/

    private static final int RC_FFT = 5;
    private Button btn_fft2main;
    private TextView tv_maxfreq,tv_maxdB;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzer);

        set_event();
        init();

        /*init params of fft and such things*/
        //analyzerActivity = new AnalyzerActivity();
        // start-later analyzerActivity.init();


    }

    void set_event()
    {
        btn_fft2main = findViewById(R.id.btn_fft2main);
        tv_maxdB = findViewById(R.id.tv_maxdB);
        tv_maxfreq = findViewById(R.id.tv_maxfreq);

        btn_fft2main.setOnClickListener(btn_click);


    }

    void update_cmd()
    {
        //TODO Control Law
    }

    void update_tv()
    {
        tv_maxdB.setText(String.valueOf(maxAmpDB));
        tv_maxfreq.setText(String.valueOf(maxAmpFreq));
    }

    View.OnClickListener btn_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exit();
            Intent intent = new Intent();
            setResult(RC_FFT,intent);
            finish();
        }
    };

    public void init()
    {
        Resources res = getResources();
        analyzerParam = new AnalyzerParameters(res);

        samplingThread = new SamplingLoop(this, analyzerParam);
        samplingThread.start();
    }

    public void exit()
    {
        if(samplingThread!=null)
            samplingThread.finish();
    }

    private class UpdateThread extends Thread
    {

    }
}
