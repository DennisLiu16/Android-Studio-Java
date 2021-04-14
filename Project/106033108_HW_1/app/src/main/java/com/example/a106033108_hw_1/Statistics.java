package com.example.a106033108_hw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {
    private int win_num;
    private int lose_num;
    private int draw_num;
    private double winRate;

    private TextView tv_win_num;
    private TextView tv_lose_num;
    private TextView tv_draw_num;
    private TextView tv_winRate;
    private TextView tv_total_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Bundle bundle = getIntent().getExtras();
        win_num = bundle.getInt("win_num");
        lose_num = bundle.getInt("lose_num");
        draw_num = bundle.getInt("draw_num");
        winRate = bundle.getDouble("winRate");

        tv_draw_num = (TextView)findViewById(R.id.tv_statistics_draw_num);
        tv_lose_num = (TextView)findViewById(R.id.tv_statistics_lose_num);
        tv_win_num = (TextView)findViewById(R.id.tv_statistics_win_num);
        tv_total_num = (TextView)findViewById(R.id.tv_statistics_total_num);
        tv_winRate = (TextView)findViewById(R.id.tv_statistics_winRate_num);

        tv_win_num.setText(Integer.toString(win_num));
        tv_lose_num.setText(Integer.toString(lose_num));
        tv_draw_num.setText(Integer.toString(draw_num));
        tv_total_num.setText(Integer.toString(win_num+lose_num+draw_num));
        double per_winRate = winRate*100;
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        tv_winRate.setText(df.format(per_winRate)+"%");
    }
}