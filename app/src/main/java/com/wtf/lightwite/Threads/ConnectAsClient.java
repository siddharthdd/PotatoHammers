package com.wtf.lightwite.Threads;

import static com.wtf.lightwite.ConstantsForApp.Constants.LOGTAG;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_CONNECTED;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_CONNECTING;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_CONNECTION_FAILED;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.wtf.lightwite.ConstantsForApp.Constants;
import com.wtf.lightwite.MainActivity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
        try {MainActivity.handler.obtainMessage(STATE_CONNECTING,device).sendToTarget();
            socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
            socket.connect();
            Log.e(LOGTAG,"Connected");
            MainActivity.handler.obtainMessage(STATE_CONNECTED,socket).sendToTarget();
        } catch (IOException | NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(LOGTAG,"Connection Failed");
            MainActivity.handler.obtainMessage(STATE_CONNECTION_FAILED,device).sendToTarget();
            } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
