    package com.example.finalcar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Formatter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public class MainActivity extends AppCompatActivity {

    /*my define*/
    private static final int REQUEST_MICROPHONE = 1;

    private static final int GOTO_DEVICELIST = 1;
    private static final int GOTO_FFT = 2;

    private static final int RC_FFT = 5;

   /*init vars*/
    private TextView tv_record,tv_device_name;
    private ImageView iv_up,iv_down,iv_left,iv_right,iv_stop;
    private Button btn_connect,btn_fft;
    private static BluetoothSend btSend;


    private char cmd;
    private char last_cmd;
    private String connect_des;

    /*activity launcher - the alternative API of startActivityForResult, which has been deprecated by google*/
    //ref : https://blog.csdn.net/yu75567218/article/details/109602407
    ActivityResultLauncher launcher = registerForActivityResult(new ResultContract(), new ActivityResultCallback<Intent>() {
        @Override
        public void onActivityResult(Intent result) {
            /*verify result intent type*/
            if(result != null)
            {
                //TODO add intent name
                Bundle bundle = result.getExtras();
                if(bundle == null) return;
                /*get name and address from return intent, and set name on textview*/
                String address = bundle.getString(DeviceList.EXTRA_DEVICE_ADDRESS);
                String name = bundle.getString(DeviceList.EXTRA_DEVICE_NAME);
                tv_device_name.setText(name);
                Toast.makeText(getApplicationContext(), name + "\n" + address, Toast.LENGTH_SHORT).show();

                /*start the connected thread, where send the cmd out*/
                BluetoothDevice device = btSend.btAdapter.getRemoteDevice(address);
                BluetoothSend.ConnectThread connect = btSend.new ConnectThread(device);
                connect.start();
            }
        }

    });

    /* The template should be <Input type,Output type> */
    class ResultContract extends ActivityResultContract<Integer, Intent> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Integer input) {
            Intent intent = null;
            switch (input)
            {
                case GOTO_DEVICELIST:
                    intent = new Intent(MainActivity.this, DeviceList.class);
                    break;

                case GOTO_FFT:
                    intent = new Intent(MainActivity.this,AnalyzerActivity.class);

                    /*stop the car while changing mode*/
                    cmd = 'S';
                    if (btSend!= null) btSend.c = cmd;
                    break;
            }
            /*this tells which intent to go*/
            return intent;
        }

        @Override
        public Intent parseResult(int resultCode, @Nullable Intent intent) {

            /*This deal with the resultCode and relative pretreatment*/
            if(resultCode == Activity.RESULT_OK)
            {
                /*from DeviceList*/
                /*set btn word to disconnect*/
                connect_des = MainActivity.this.getString(R.string.btn_disconnect);
                btn_connect.setText(connect_des);
                return intent;
            }

            else if(resultCode == RC_FFT)
            {
                /*from FFT*/
                return intent;
            }
            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*set events*/
        set_event();
        /*init connect des*/
        connect_des = this.getString(R.string.btn_connect);
    }

    void set_event()
    {
        tv_record = findViewById(R.id.tv_record);
        tv_device_name = findViewById(R.id.tv_device_name);
        iv_up = findViewById(R.id.iv_up);
        iv_down = findViewById(R.id.iv_down);
        iv_right = findViewById(R.id.iv_right);
        iv_left = findViewById(R.id.iv_left);
        iv_stop = findViewById(R.id.iv_stop);
        btn_connect = findViewById(R.id.btn_connect);
        btn_fft = findViewById(R.id.btn_fft);

        btn_connect.setOnClickListener(btn_click);
        btn_fft.setOnClickListener(btn_click);

        iv_up.setOnClickListener(iv_click);
        iv_down.setOnClickListener(iv_click);
        iv_right.setOnClickListener(iv_click);
        iv_left.setOnClickListener(iv_click);
        iv_stop.setOnClickListener(iv_click);

        iv_up.setOnLongClickListener(iv_click_long);
        iv_down.setOnLongClickListener(iv_click_long);
        iv_right.setOnLongClickListener(iv_click_long);
        iv_left.setOnLongClickListener(iv_click_long);
        iv_stop.setOnLongClickListener(iv_click_long);

        tv_record.setMovementMethod(ScrollingMovementMethod.getInstance());

        btSend = new BluetoothSend(this);

        /* sound permission*/
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);

        }
    }



    private View.OnClickListener btn_click = v -> {

        /* v == btn_connect*/
        switch (v.getId()) {
            case R.id.btn_connect:
                /*change to bt device list*/
                if (connect_des == this.getString(R.string.btn_connect)) {
                    /*check btSend not null */
                    if (btSend == null) btSend = new BluetoothSend(this);

                    /*launch DeviceList Activity*/
                    launcher.launch(GOTO_DEVICELIST);
                } else if (connect_des == this.getString(R.string.btn_disconnect)) {
                    /*exit key*/
                    btSend.should_exit = true;
                    btSend = null;

                    /*update btn word to connect*/
                    connect_des = this.getString(R.string.btn_connect);
                    btn_connect.setText(connect_des);

                    /*record disconnect*/
                    tv_record.append("Disconnect   \n");

                    /*clean tv name*/
                    tv_device_name.setText("");
                }
                break;
            case R.id.btn_fft:
                launcher.launch(GOTO_FFT);
        }

    };

    private View.OnClickListener iv_click = v -> {
        /*short click -> stop only*/
        cmd = 'S';
        alpha_reset();
        v.setAlpha(1);
        record();
        if (btSend!= null) btSend.c = cmd;
        last_cmd = cmd;
    };

    private View.OnLongClickListener iv_click_long = v ->
    {
        /*iv long click*/
        switch(v.getId())
        {
            case R.id.iv_up: cmd='F';break;
            case R.id.iv_down: cmd='B';break;
            case R.id.iv_right: cmd='R';break;
            case R.id.iv_left: cmd='L';break;
            case R.id.iv_stop: cmd='S';break;
        }

        /*set transparency*/
        if(cmd != last_cmd)
        {
            alpha_reset();
            v.setAlpha(1);
        }
        record();
        /*update cmd*/
        if (btSend!= null) btSend.c = cmd;
        last_cmd = cmd;

        /*return true to end the event, not calling the OnClickListener*/
        return true;
    };

    /*reset transparency to unclicked state*/
    void alpha_reset()
    {
        iv_up.setAlpha((float)0.5);
        iv_down.setAlpha((float)0.5);
        iv_right.setAlpha((float)0.5);
        iv_left.setAlpha((float)0.5);
        iv_stop.setAlpha((float)0.5);
    }

    /*record the cmd*/
    void record()
    {
        /*get time*/
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);

        /*trans record data to string and append*/
        String timestamp = new Formatter().format("%02d:%02d:%02d",hour,min,sec).toString();
        String s = timestamp + " - cmd : " + cmd + "    \n";
        tv_record.append(s);

        /*set cursor to bottom*/
        int offset = tv_record.getLineCount()*tv_record.getLineHeight();
        if(offset > tv_record.getLineHeight())
            tv_record.scrollTo(0,offset-tv_record.getHeight());
    }

    public static BluetoothSend getBluetoothSend()
    {
        return btSend;
    }

}