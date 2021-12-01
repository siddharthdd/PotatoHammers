package com.wtf.lightwite.Threads;


import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.wtf.lightwite.ConstantsForApp.Constants;
import com.wtf.lightwite.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ReceiveBluetooth extends Thread implements Constants {
    BluetoothSocket socket;
    InputStream inputStream;
    InputStream inpTemp;
    OutputStream outputStream;
    byte[] buffer;
    int bytes;
    public ReceiveBluetooth(BluetoothSocket socket){
        this.socket = socket;
       inpTemp = null;
        OutputStream outTemp = null;
        try {
            inpTemp = socket.getInputStream();
            outTemp = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = inpTemp;
        outputStream = outTemp;
    }    @Override
    public void run() {
        buffer = new byte[1024];
            try {
                bytes = inputStream.read(buffer);
                MainActivity.handler.obtainMessage(STATE_MESSAGE_RECIVED,bytes,-1,buffer).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }

           }
           public void write(byte[] data){
            try {
                outputStream.write(data,0, data.length);
                String str = new String(data, StandardCharsets.UTF_8);
                Log.e(LOGTAG,str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(LOGTAG, "Could not close the connect socket", e);
        }
    }

}
