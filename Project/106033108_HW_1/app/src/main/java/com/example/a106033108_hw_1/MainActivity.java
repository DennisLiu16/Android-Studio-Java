package com.example.a106033108_hw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_main;
    private TextView tv_main_mode;

    private Button btn_mora;
    private Button btn_guess_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*link elements to code */
        tv_main = (TextView)findViewById(R.id.tv_main);
        tv_main_mode = (TextView)findViewById(R.id.tv_main_mode);
        btn_mora = (Button)findViewById(R.id.btn_mora);
        btn_guess_num = (Button)findViewById(R.id.btn_guess_num);

        btn_mora.setOnClickListener(btn_gotoMora);
        btn_guess_num.setOnClickListener(btn_gotoGuessNum);
    }

    private View.OnClickListener btn_gotoMora = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,MoraActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener btn_gotoGuessNum = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,GuessNumActivity.class);
            startActivity(intent);
        }
    };
}