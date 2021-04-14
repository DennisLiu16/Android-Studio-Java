package com.example.a106033108_hw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
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
    private TextView tv_mora_total_num;
    private Button btn_mora_go_to_mora;
    private int[] picture = {R.drawable.rock,R.drawable.scissors,R.drawable.paper};
    /*code vars*/
    private int userFinger;
    private int robotFinger;
    private int win_num = 0;
    private int lose_num = 0;
    private int draw_num = 0;
    private double win_rate = 0.0;

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
        tv_mora_total_num = (TextView)findViewById(R.id.tv_total_num);
        btn_mora_go_to_mora = (Button)findViewById(R.id.btn_go_to_statistics);

        /*set Events Listeners*/
        iv_mora_scissors.setOnClickListener(imageClick);
        iv_mora_paper.setOnClickListener(imageClick);
        iv_mora_rock.setOnClickListener(imageClick);
        btn_mora_go_to_mora.setOnClickListener(go_to_statistics);
        String str = "第"+Integer.toString(win_num+lose_num+draw_num+1)+"局";
        tv_mora_total_num.setText(str);
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
            if(result == -1 || result == 2) {
                tv_mora_sug.setText("恭喜你獲勝~~");
                win_num++;
            }
            else if(result == 0) {
                tv_mora_sug.setText("平手");
                draw_num++;
            }
            else {
                tv_mora_sug.setText("Sorry，你輸了");
                lose_num++;
            }
            String str = "第"+Integer.toString(win_num+lose_num+draw_num+1)+"局";
            tv_mora_total_num.setText(str);
        }
    };

    private View.OnClickListener go_to_statistics = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // send bundle
            win_rate = (double)win_num/(win_num+lose_num);
            Bundle bundle = new Bundle();
            bundle.putInt("win_num",win_num);
            bundle.putInt("lose_num",lose_num);
            bundle.putInt("draw_num",draw_num);
            bundle.putDouble("winRate",win_rate);

            Intent intent = new Intent();
            intent.setClass(MoraActivity.this,Statistics.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
}