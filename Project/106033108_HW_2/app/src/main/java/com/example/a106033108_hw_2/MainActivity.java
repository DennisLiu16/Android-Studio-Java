package com.example.a106033108_hw_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    /*Layout Related*/
    private RelativeLayout rl;
    private View container_start;
    private View container_running;
    private TextView tv_Maintimer;
    private TextView tv_SubTimer;
    private Button btn_start;
    private Button btn_pause;
    private Button btn_reset;
    /*thread - Timer and Task*/
    private Timer Schedule = null;
    private TimerTask task_main = null;
    private TimerTask task_sub = null;
    /*My timer*/
    private mTimer MainTimer;
    private mTimer SubTimer = null;
    /*TimeTask send msg to handler*/
    private Handler handler;
    /*var*/
    private int sub_cycle = 1;
    private final int Update_Both_Tv = 0;
    private final int Update_Main_Tv = 1;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*set actionbar(return btn)*/
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*elements link*/
        rl = (RelativeLayout) findViewById(R.id.rl);
        tv_Maintimer = (TextView)findViewById(R.id.tv_Maintimer);
        tv_SubTimer = (TextView)findViewById(R.id.tv_SubTimer);

        container_start = LayoutInflater.from(this).inflate(R.layout.start_layout,rl,false);
        container_running = LayoutInflater.from(this).inflate(R.layout.running_layout,rl,false);

        /*add start page view with relative layout*/
        rl.addView(container_start);

        /*set btn related - start*/
        btn_start = (Button)container_start.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(btn_start_click);
        /*set btn related - running*/
        btn_pause = (Button)container_running.findViewById(R.id.btn_pause);
        btn_reset = (Button)container_running.findViewById(R.id.btn_reset);
        btn_pause.setOnClickListener(btn_running_click);
        btn_reset.setOnClickListener(btn_running_click);

        /*set handler*/
        handler = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String str = new String();
                switch (msg.arg1)
                {
                    case(Update_Both_Tv):
                        str = SubTimer.getElapsedString();
                        tv_SubTimer.setText(str);
                    case(Update_Main_Tv):
                        str = MainTimer.getElapsedString();
                        tv_Maintimer.setText(str);
                        break;
                }
            }
        };

    }
    /*ActionBar Click Listeners*/
    /**ref : https://www.ruyut.com/2018/12/android-studio_21.html**/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*Button Click Listeners*/
    private View.OnClickListener btn_start_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*change layout to running*/
            rl.removeAllViews();
            rl.addView(container_running);
            /*mTimer and schedule*/
            MainTimer = mTimer.init();
            MainTimer.start();
            Schedule = new Timer();
            set_schedule();
        }
    };

    private View.OnClickListener btn_running_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean _isrunning = MainTimer.get_isrunning();
            if(v.getId() == btn_pause.getId())
            {
                if(_isrunning)
                {
                    /*@pause*/
                    /*pause -> continue*/
                    MainTimer.pause();
                    if(SubTimer != null)
                        SubTimer.pause();
                    btn_pause.setText(R.string.btn_continue_str);
                    btn_pause.setBackgroundResource(R.drawable.btn_start_shape);
                    btn_reset.setText(R.string.btn_reset_str);
                    unset_schedule();

                    /*cancel scheduled task*/
                    //subtimer case
                }
                else {
                    /*@continue*/
                    /*continue -> pause*/
                    MainTimer.start();
                    if(SubTimer != null)
                        SubTimer.start();
                    btn_pause.setText(R.string.btn_pause_str);
                    btn_reset.setText(R.string.btn_single_str);
                    btn_pause.setBackgroundResource(R.drawable.btn_pause_shape);

                    /*set both scheduled task*/
                    Schedule = new Timer();
                    set_schedule();
                }

            }
            else
            {
                if(_isrunning)
                {
                    /*@single log*/
                    if(SubTimer == null)
                    {
                        /*log and create SubTimer*/
                        timerLog(MainTimer);
                        SubTimer = mTimer.init();
                    }
                    else
                    {
                        /*log and update SubTimer*/
                        timerLog(SubTimer);
                        SubTimer.reset();
                    }

                    /*start or restart the timer*/
                    SubTimer.start();
                    set_schedule();
                    /*update sub_cycle*/
                    sub_cycle += 1;
                }
                else
                {
                    /*@reset*/
                    /*cancel all timer object*/
                    btn_pause.setText(R.string.btn_pause_str);
                    btn_reset.setText(R.string.btn_single_str);
                    btn_pause.setBackgroundResource(R.drawable.btn_pause_shape);

                    /*unset TimerTask and Timer*/
                    unset_schedule();

                    SubTimer = null;
                    MainTimer = null;
                    Schedule = null;
                    task_main = null;
                    task_sub = null;

                    /*init sub_cycle*/
                    sub_cycle = 1;

                    /*rl update*/
                    rl.removeAllViews();
                    rl.addView(container_start);
                    tv_Maintimer.setText(R.string.tv_timer_init_str);
                    tv_SubTimer.setText("");
                }
            }

        }
    };

    /*TBD*/
    private void timerLog(mTimer mt)
    {
        /*get correct logger*/
        String log = mt.getElapsedString();

        /*update to correct column*/
    }

    /*set Timer schedule*/
    private void set_schedule()
    {
        if(task_sub==null && SubTimer != null)
        {
            /*df TimerTask-Sub*/
            task_sub = new TimerTask() {
                @Override
                public void run() {
                    /*send msg to handler*/
                    Message msg = new Message();
                    msg.arg1 = Update_Both_Tv;
                    handler.sendMessage(msg);
                }
            };
            /*make task work at fixed rate*/
            Schedule.scheduleAtFixedRate(task_sub,0,1);

        }
        /*Main Task Set*/
        else if(task_main == null && MainTimer != null)
        {
            task_main = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.arg1 = Update_Main_Tv;
                    handler.sendMessage(msg);
                }
            };
            Schedule.scheduleAtFixedRate(task_main,0,1);
        }
    }

    /*delete TimeTask and Timer*/
    private void unset_schedule()
    {
        if(task_sub!=null)
        {
            /*delete Subtask*/
            task_sub.cancel();
            task_sub = null;
        }
        if(task_main!=null)
        {
            task_main.cancel();
            task_main = null;
        }
        Schedule.cancel();
    }

}

