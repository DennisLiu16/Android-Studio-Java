package com.example.canvus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MyCanvas mcanvas;
    private Button btn_clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* create Mycanvas */
        mcanvas = (MyCanvas)findViewById(R.id.cv_main);
        /* create Clear Button */
        btn_clean = (Button)findViewById(R.id.btn_clean);
        /* set event listener*/
        btn_clean.setOnClickListener(clean);
    }

    private View.OnClickListener clean = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /* call public Mycanvas function - clear() */
            mcanvas.Clear();
        }
    };
}