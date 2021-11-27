package com.wtf.lightwite.Threads;

import static com.wtf.lightwite.ConstantsForApp.Constants.LOGTAG;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_MESSAGE_RECIVED;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.wtf.lightwite.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReceiveBluetooth extends Thread{
    BluetoothSocket socket;
    InputStream inputStream;
    OutputStream outputStream;
    byte[] buffer;
    int bytes;
    public ReceiveBluetooth(BluetoothSocket socket){
        this.socket = socket;
        InputStream inpTemp = null;
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
        while (true){
            try {
                bytes = inputStream.read(buffer,0,buffer.length);
                MainActivity.handler.obtainMessage(STATE_MESSAGE_RECIVED,bytes,-1,buffer).sendToTarget();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }    }
        public void write(byte[] data){
            try {
                outputStream.write(data,0, data.length);
                Log.e(LOGTAG,data.toString());
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
