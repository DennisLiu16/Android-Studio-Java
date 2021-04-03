package com.example.exercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /*vars*/
    private Button Button1;
    private TextView Textviwe1;
    private int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*link Element to Code , view type*/
        Button1 = (Button)findViewById(R.id.calculate_button);
        Textviwe1 = (TextView)findViewById(R.id.answer_textview);

        /*set event listener*/
        Button1.setOnClickListener(btnclick);
    }

    /*Implement*/
    private View.OnClickListener btnclick = (view) -> {
        for(int i = 1;i <= 10;i++)
        {
            sum = sum+i;
        }
        Textviwe1.setText("答案是"+Integer.toString(sum));
    };

    /* What is (view) -> means?
    *
    * It's Syntactic sugar of lambda onclick function
    *
    * Also as
    *
   private View.OnClickListener btnclick = new View.OnClickListener(){
    public void onClick(View view){
    for (int i = 1 ; i<=10 ; i++ )
    {
        sum = sum +i;
    }
    Textview1.setText("答案="+Integer.toString(sum));
    }
};

    * */
}