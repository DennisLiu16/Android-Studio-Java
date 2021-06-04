package com.example.finalcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class fftActivity extends AppCompatActivity {

    private Button btn_fft2main;
    private TextView tv_maxfreq,tv_maxdB;
    public AnalyzerActivity analyzerActivity = null;
    int a = 1;

    private static final int RC_FFT = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fft);

        set_event();

        /*init params of fft and such things*/
        analyzerActivity = new AnalyzerActivity();
        // start-later analyzerActivity.init();


        UpdateThread update = new UpdateThread(analyzerActivity);
        update.start();

    }

    public class UpdateThread extends Thread{

        private AnalyzerActivity _analyzerActivity;
        private Boolean is_running = false;

        UpdateThread(AnalyzerActivity analyzerActivity)
        {
            _analyzerActivity = analyzerActivity;
            is_running = true;
        }

        @Override
        public void run() {
            super.run();
            while(is_running)
            {
                update_tv();
                update_cmd();
            }

        }
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
        a++;
        tv_maxdB.setText(String.valueOf(a));
       // tv_maxdB.setText(String.valueOf(analyzerActivity.maxAmpDB));
        tv_maxfreq.setText(String.valueOf(analyzerActivity.maxAmpFreq));
    }

    View.OnClickListener btn_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            analyzerActivity.exit();
            Intent intent = new Intent();
            setResult(RC_FFT,intent);
            finish();
        }
    };
}