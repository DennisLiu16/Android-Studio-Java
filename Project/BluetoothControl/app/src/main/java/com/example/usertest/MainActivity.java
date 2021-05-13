package com.example.usertest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public char c;

    public Button mBtnConnect, mBtnF, mBtnB, mBtnL, mBtnR;
    public TextView mTextView;
    public BluetoothSend bluetoothSend;

    private static final int REQUEST_CONNECT_DEVICE_SECURE= 1;
    public static final int REQUEST_ENABLE_BT=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);
        mBtnConnect = findViewById(R.id.buttonConnect);
        mBtnConnect.setOnClickListener(btnConnect);
        mBtnF = findViewById(R.id.buttonF);
        mBtnF.setOnClickListener(btn);
        mBtnF.setOnLongClickListener(btnLong);

        mBtnB = findViewById(R.id.buttonB);
        mBtnB.setOnClickListener(btn);
        mBtnB.setOnLongClickListener(btnLong);

        mBtnL = findViewById(R.id.buttonL);
        mBtnL.setOnClickListener(btn);
        mBtnL.setOnLongClickListener(btnLong);

        mBtnR = findViewById(R.id.buttonR);
        mBtnR.setOnClickListener(btn);
        mBtnR.setOnLongClickListener(btnLong);

        bluetoothSend = new BluetoothSend(this, mTextView);
    }


    public View.OnClickListener btnConnect = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
            startActivityForResult(intent, REQUEST_CONNECT_DEVICE_SECURE);
        }
    };

    public View.OnClickListener btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            c = 'S';
            bluetoothSend.c = c;
        }
    };

    public View.OnLongClickListener btnLong = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()){
                case R.id.buttonF: c = 'F'; break;
                case R.id.buttonB: c = 'B'; break;
                case R.id.buttonL: c = 'L'; break;
                case R.id.buttonR: c = 'R'; break;
                default: c = 'S'; break;
            }
            bluetoothSend.c = c;
            return false;
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK)
                    Log.d("BT: ", "BT enabled");
                break;
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK){
                    Bundle bun = data.getExtras();
                    if (bun == null) return;
                    String address = bun.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    String name = bun.getString(DeviceListActivity.EXTRA_DEVICE_NAME);
                    mTextView.setText(name);
                    Toast.makeText(getApplicationContext(), name + "\n" + address, Toast.LENGTH_SHORT).show();

                    BluetoothDevice device = bluetoothSend.btAdapter.getRemoteDevice(address);
                    BluetoothSend.ConnectThread connect = bluetoothSend.new ConnectThread(device);
                    connect.start();
                }
                break;
        }
    }

}
