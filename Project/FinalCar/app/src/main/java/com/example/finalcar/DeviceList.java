package com.example.finalcar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class DeviceList extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        setResult(Activity.RESULT_CANCELED);

        ArrayAdapter<String> pairedDevicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,0);
        ListView listPairedDevices = findViewById(R.id.listViewPairedDevices);
        listPairedDevices.setAdapter(pairedDevicesArrayAdapter);
        listPairedDevices.setOnItemClickListener(mDeviceClickListener);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0){
            for (BluetoothDevice device : pairedDevices){
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        else{
            String noDevices = "No devices have been paired.";
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            String name = info.substring(0, info.length() - 18);

            Intent intent = new Intent();
            /*send extra data*/
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            intent.putExtra(EXTRA_DEVICE_NAME, name);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
}
