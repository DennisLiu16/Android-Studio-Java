package com.example.a106033108_hw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
/*
* @ 0 as rock , 1 as scissors , 2 as paper
*
* */

public class MoraActivity extends AppCompatActivity {

    /*code elements vars*/
    private ImageView iv_mora_robot;
    private ImageView iv_mora_scissors;
    private ImageView iv_mora_paper;
    private ImageView iv_mora_rock;
    private TextView tv_mora_sug;
    private int[] picture = {R.drawable.rock,R.drawable.scissors,R.drawable.paper};
    /*code vars*/
    private int userFinger;
    private int robotFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mora);

        /*link elements*/
        iv_mora_paper = (ImageView)findViewById(R.id.iv_mora_paper);
        iv_mora_rock = (ImageView)findViewById(R.id.iv_mora_rock);
        iv_mora_scissors = (ImageView)findViewById(R.id.iv_mora_scissors);
        iv_mora_robot = (ImageView)findViewById(R.id.iv_mora_robot);
        tv_mora_sug = (TextView)findViewById(R.id.tv_mora_sug);

        /*set Events Listeners*/
        iv_mora_scissors.setOnClickListener(imageClick);
        iv_mora_paper.setOnClickListener(imageClick);
        iv_mora_rock.setOnClickListener(imageClick);
    }

    private View.OnClickListener imageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /*Robot Generate*/
            robotFinger = (int)(Math.random()*3);
            iv_mora_robot.setImageResource(picture[robotFinger]);

            /*get which image was clicked*/
            if(v.getId() == R.id.iv_mora_rock)
                userFinger = 0;
            else if(v.getId() == R.id.iv_mora_scissors)
                userFinger = 1;
            else
                userFinger = 2;



            /*judge stage*/
            int result = userFinger - robotFinger;
            if(result == -1 || result == 2)
                tv_mora_sug.setText("恭喜你獲勝~~");
            else if(result == 0)
                tv_mora_sug.setText("平手");
            else
                tv_mora_sug.setText("Sorry，你輸了");
        }
    };
}