package com.example.a106033108_hw_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button btn_start;
    private TextView tv_timer;
    private mTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*elements link*/
        btn_start = (Button)findViewById(R.id.btn_start);
        tv_timer = (TextView)findViewById(R.id.tv_timer);



    }
}