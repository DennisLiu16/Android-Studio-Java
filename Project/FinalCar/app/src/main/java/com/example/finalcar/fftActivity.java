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
    private

    private static final int RC_FFT = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fft);

        set_event();

        /*init params of fft and such things*/
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

    View.OnClickListener btn_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            setResult(RC_FFT,intent);
            finish();
        }
    };
}