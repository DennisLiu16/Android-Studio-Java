package com.example.a106033108_hw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class GuessNumActivity extends AppCompatActivity {

    /*
    * @def_range : 2~98 acceptable , also can express as min+1 ~ max-1
    *
    * */

    /*code elements vars*/
    private TextView tv_guessnum_sug;
    private TextView tv_guessnum_numbar;
    private TextView tv_guessnum_biasbar;
    private EditText et_guessnum_numinput;
    private Button btn_guessnum_start;
    private Button btn_guessnum_guess;
    private SeekBar sb_guessnum_numbar;
    private SeekBar sb_guessnum_biasbar;

    /*code vars*/
    private int answer;
    private int guess_num;     // the val compared to answer
    private int numbar;        // main seekbar num
    private int biasbar;       // sub seekbar num
    private int max = 99;      // limit the maximum of the guess range , not including
    private int min = 1;       // limit the minimum of the guess range , not including
    private int up_limit;      // guess range [down_limit,up_limit]
    private int down_limit;

    private boolean start_flag = false;

    private String sug_str;

    /*Constructor*/
    public GuessNumActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_num);

        /*link elements*/
        tv_guessnum_sug = (TextView)findViewById(R.id.tv_guessnum_sug);
        tv_guessnum_numbar = (TextView)findViewById(R.id.tv_guessnum_numbar);
        tv_guessnum_biasbar = (TextView)findViewById(R.id.tv_guessnum_biasbar);
        et_guessnum_numinput = (EditText)findViewById(R.id.et_guessnum_numinput);
        btn_guessnum_guess = (Button)findViewById(R.id.btn_guessnum_guess);
        btn_guessnum_start = (Button)findViewById(R.id.btn_guessnum_start);
        sb_guessnum_numbar = (SeekBar)findViewById(R.id.sb_guessnum_numbar);
        sb_guessnum_biasbar = (SeekBar)findViewById(R.id.sb_guessnum_biasbar);

        /*set Events Listeners*/
        btn_guessnum_start.setOnClickListener(btnStart_Click);
        btn_guessnum_guess.setOnClickListener(btnGuess_Click);
        sb_guessnum_numbar.setOnSeekBarChangeListener(sbNum_main);
        sb_guessnum_biasbar.setOnSeekBarChangeListener(sbNum_bias);
    }

    private View.OnClickListener btnStart_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            start_flag = true;
            up_limit = max - 1;
            down_limit = min + 1;

            /*Clean and Reset*/
            update_seekbar();
            tv_guessnum_sug.setText(R.string.et_guessnum_hint_str);
            sb_guessnum_numbar.setMax(up_limit - down_limit);

            /*Generate answer*/
            answer = (int)(Math.random()*(up_limit-down_limit + 1) + down_limit);
            btn_guessnum_start.setText(R.string.btn_guessnum_restart_str);

        }
    };

    private View.OnClickListener btnGuess_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(start_flag) {
                if (!et_guessnum_numinput.getText().toString().isEmpty()) {
                    guess_num = Integer.parseInt(et_guessnum_numinput.getText().toString());

                    /*range check*/
                    if (guess_num >= down_limit && guess_num <= up_limit) {

                        if (guess_num == answer) {
                            start_flag = false;
                            sug_str = "恭喜答對，密碼是:";
                            tv_guessnum_sug.setText(sug_str + Integer.toString(guess_num));
                        } else if (guess_num < answer) {
                            down_limit = guess_num+1;
                            sug_str = "答錯了，範圍是:";
                            tv_guessnum_sug.setText(Integer.toString(guess_num)+sug_str + Integer.toString(down_limit-1) + "~" + Integer.toString(up_limit+1));
                        } else {
                            up_limit = guess_num-1;
                            sug_str = "答錯了，範圍是:";
                            tv_guessnum_sug.setText(Integer.toString(guess_num)+sug_str + Integer.toString(down_limit-1) + "~" + Integer.toString(up_limit+1));
                        }

                        update_seekbar();
                    }
                    /*out of range*/
                    else{
                        sug_str = "您的輸入已超過範圍，範圍是:";
                        tv_guessnum_sug.setText(sug_str + Integer.toString(down_limit) + "~" + Integer.toString(up_limit));
                    }


                } else {
                    sug_str = "請輸入想猜測的值";
                    tv_guessnum_sug.setText(sug_str);
                }
            }
            else
            {
                sug_str = "請選擇開始";
                tv_guessnum_sug.setText(sug_str);
            }


        }
    };

    private SeekBar.OnSeekBarChangeListener sbNum_main = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int pre = seekBar.getProgress();
            numbar = pre + down_limit;
            tv_guessnum_numbar.setText(String.valueOf(numbar));
            clean_bias_seekbar();
            et_guessnum_numinput.setText(String.valueOf(numbar));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            /*Empty*/
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            /*Empty*/
        }
    };

    private SeekBar.OnSeekBarChangeListener sbNum_bias = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int pre = seekBar.getProgress();
            biasbar = pre - seekBar.getMax()/2;
            if ( (numbar + biasbar > up_limit || numbar + biasbar < down_limit) && start_flag)
            {
                sug_str = "您的輸入已超過範圍，範圍是:";
                tv_guessnum_sug.setText(sug_str + Integer.toString(down_limit) + "~" + Integer.toString(up_limit));
            }
            else
            {
                tv_guessnum_biasbar.setText(String.valueOf(biasbar));
                et_guessnum_numinput.setText(String.valueOf(numbar+biasbar));
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            /*Empty*/
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            /*Empty*/
        }
    };

    private void update_seekbar()
    {
        /*set bias 0*/
        clean_bias_seekbar();

        /*set numbar*/
        set_numbar();
    }

    private void clean_bias_seekbar()
    {
        sb_guessnum_biasbar.setProgress(sb_guessnum_biasbar.getMax()/2);
        tv_guessnum_biasbar.setText(String.valueOf(0));
    }

    private void set_numbar()
    {
        sb_guessnum_numbar.setMax(up_limit-down_limit);
        sb_guessnum_numbar.setProgress( (up_limit-down_limit)/2 );
    }

}