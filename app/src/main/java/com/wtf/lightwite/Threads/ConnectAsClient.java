package com.wtf.lightwite.Threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.wtf.lightwite.ConstantsForApp.Constants;

import java.io.IOException;
import java.util.UUID;

public class ConnectAsClient extends Thread {
    BluetoothDevice device;
    BluetoothSocket socket;
    BluetoothAdapter bluetoothAdapter;
    public ConnectAsClient(BluetoothDevice device){
        this.device = device;
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.UUID_STRING));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();
        try {
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("App Debug","Connection Failed");
            }
        //Send Recieve
    }
}
