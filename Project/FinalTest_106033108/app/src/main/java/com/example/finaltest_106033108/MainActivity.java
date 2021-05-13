package com.example.finaltest_106033108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    static boolean active;
    private int value;
    private int draw_value;
    private int[] StoreArray;

    private TextView tv_value;
    private SeekBar sb_value;
    private Button btn_change;

    public Timer timer = null;
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_value = findViewById(R.id.tv_value);
        sb_value = findViewById(R.id.sb_value);
        btn_change = findViewById(R.id.btn_change);
        lineChart = findViewById(R.id.lc_value);

        btn_change.setOnClickListener(Change);
        sb_value.setOnSeekBarChangeListener(getValue);
        StoreArray = new int[10];

        /*set timer 100ms*/
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                boolean active = ((Global)getApplicationContext()).get();
                if(active)
                {
                    draw_value = value;
                    if(StoreArray.length < 10)
                    {
                        StoreArray[StoreArray.length] = draw_value;
                    }
                    else
                    {
                        for(int i = 0;i < 9;i++)
                        {
                            StoreArray[i] = StoreArray[i+1];
                        }
                        StoreArray[9] = draw_value;
                    }

                    /*update dateset*/
                    List<Entry>list=new ArrayList<>();
                    for(int i = 0;i<StoreArray.length;i++)
                    {
                        list.add(new Entry(i,StoreArray[i]));
                    }
                    LineDataSet dataset = new LineDataSet(list,"store");
                    dataset.setDrawFilled(true);
                    dataset.setFillColor(Color.parseColor("#FFFD464E"));
                    dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    LineData linedata = new LineData(dataset);
                    lineChart.setData(linedata);

                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                }
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task,0,100);
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Global)getApplication()).set(false);
    }

    private View.OnClickListener Change = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /*stop timer*/
            ((Global)getApplication()).set(false);
            /*go to store*/
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,Store.class);
            Bundle bundle = new Bundle();
            bundle.putIntArray("StoreArray",StoreArray);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private SeekBar.OnSeekBarChangeListener getValue = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            value = sb_value.getProgress();
            tv_value.setText(String.valueOf(value));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

}