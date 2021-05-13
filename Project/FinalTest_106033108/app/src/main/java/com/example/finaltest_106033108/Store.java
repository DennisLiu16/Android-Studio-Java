package com.example.finaltest_106033108;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Store extends AppCompatActivity {
    private int[] StoreArray;
    private TextView tv_store;
    private Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Bundle bundle = this.getIntent().getExtras();
        StoreArray = bundle.getIntArray("StoreArray");
        tv_store = findViewById(R.id.tv_store);
        btn_back = findViewById(R.id.btn_back);

        /*show data*/
        tv_store.setText("");
        int length = StoreArray.length;

        for(int i = 0;i<length;i++)
        {
            tv_store.append(String.valueOf(StoreArray[i]));
            tv_store.append("\n");
        }
        btn_back.setOnClickListener(Back);
    }

    private View.OnClickListener Back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Global)getApplication()).set(true);
            Store.this.finish();
        }
    };
}