package com.example.exercise2_bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    /*vars*/
    private Button calculate_button;
    private EditText name_enter,height_enter,weight_enter;
    private TextView suggestion,bmi_result;
    private String name,sug_str;
    private float H,W,BMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*link elements to code*/
        calculate_button = (Button)findViewById(R.id.calculate_bmi);
        name_enter = (EditText)findViewById(R.id.name_editText);
        height_enter = (EditText)findViewById(R.id.height_editText);
        weight_enter = (EditText)findViewById(R.id.weight_editText);
        suggestion = (TextView)findViewById(R.id.suggestion);
        bmi_result = (TextView)findViewById(R.id.bmi_result);

        /*set Listener*/
        calculate_button.setOnClickListener(btnclick);
    }

    private View.OnClickListener btnclick = (view) ->
    {
        H = Float.parseFloat(height_enter.getText().toString());
        W = Float.parseFloat(weight_enter.getText().toString());
        name = name_enter.getText().toString();
        BMI = W / (H * H);

        bmi_result.setText(name + "，您的BMI是:" + new DecimalFormat("0.00").format(BMI));
        if (BMI >= 25) {
            sug_str = this.getString(R.string.overweight_sug);
        } else if (BMI <= 18) {
            sug_str = this.getString(R.string.underweight_sug);
        } else {
            sug_str = this.getString(R.string.fit_sug);
        }

        suggestion.setText(sug_str);
    };
}