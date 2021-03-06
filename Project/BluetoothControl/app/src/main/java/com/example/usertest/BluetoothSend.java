package com.example.usertest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothSend {
    public Context context;

    public char c = 'S';

    public BluetoothAdapter btAdapter;
    public TextView mTextView;

    private static final int SUCCESS_CONNECT = 0;
    public static final int REQUEST_ENABLE_BT=3;
    private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothSend(Context context, TextView mTextView) {
        this.context = context;
        this.mTextView = mTextView;

        bluetoothInitial();
    }

    public void bluetoothInitial(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter==null){
            Toast.makeText(context, "No bluetooth detected", Toast.LENGTH_SHORT).show();
        }
        else{
            if(!btAdapter.isEnabled()){
                Intent intent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity)context).startActivityForResult(intent,REQUEST_ENABLE_BT);
                Toast.makeText(context, "Bluetooth on", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Bluetooth is already on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what == SUCCESS_CONNECT) {
                BluetoothSend.ConnectedThread connected = new BluetoothSend.ConnectedThread((BluetoothSocket) msg.obj);
                connected.start();
            }
        }
    };

    public class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device){
            BluetoothSocket tmp = null;
            try{
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            } catch (IOException e){
                Log.e("connect: ", "failed", e);
            }
            mmSocket = tmp;
        }

        public void run(){
            btAdapter.cancelDiscovery();
            try{
                mmSocket.connect();
            }catch (IOException e){
                try{
                    mmSocket.close();
                }catch (IOException e2){
                    Log.e("connect: ", "socket during connection failure", e2);
                }
                return;
            }
            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
        }
    }

    public class ConnectedThread extends Thread{
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try{
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            }catch(IOException e){
                Log.e("connected: ", "IOStream",e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            while (true){
                try{
                    mmOutStream.write((byte)c);
                    mmOutStream.flush();
                    Log.d("connected: ", "sending");
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        Log.e("connected: ","error in reading buffer");
                    }
                }catch(IOException e){
                    Log.e("connected: ", "Output",e);
                }
            }
        }
    }

    public ~BluetoothSend
}
