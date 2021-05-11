package com.example.fileio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    /*Elements*/
    private TextView tv_show_content;
    private Button btn_read;
    private Button btn_write;
    private Button btn_clean;
    private EditText et_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    private void init()
    {
        tv_show_content = (TextView)findViewById(R.id.tv_show_content);
        btn_read = (Button)findViewById(R.id.btn_read);
        btn_write = (Button)findViewById(R.id.btn_write);
        btn_clean = (Button)findViewById(R.id.btn_clean);
        et_input = (EditText)findViewById(R.id.et_input);

        btn_read.setOnClickListener(read);
        btn_write.setOnClickListener(write);
        btn_clean.setOnClickListener(clean);
    }

    private View.OnClickListener read = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                FileInputStream fileIn = openFileInput("DATA");
                BufferedInputStream bufFileIn = new BufferedInputStream(fileIn);

                byte[] bufBytes = new byte[10];
                tv_show_content.setText("");
                do {
                    int c = bufFileIn.read(bufBytes);
                    if(c == -1)
                        break;
                    else
                        tv_show_content.append(new String(bufBytes));
                }while(true);

                bufFileIn.close();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private View.OnClickListener write = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FileOutputStream fileOut = null;
            BufferedOutputStream bufFileOut = null;
            try {
                fileOut = openFileOutput("DATA",MODE_APPEND);
                bufFileOut = new BufferedOutputStream(fileOut);
                bufFileOut.write(et_input.getText().toString().getBytes()); // tf to byte type
                bufFileOut.write("\r\n".getBytes());
                bufFileOut.close();
                et_input.setText("");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener clean = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FileOutputStream fileOut = null;
            try {
                fileOut = openFileOutput("DATA",MODE_PRIVATE);
                fileOut.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}