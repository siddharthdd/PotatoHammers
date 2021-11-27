package com.wtf.lightwite.Threads;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReceiveBluetooth extends Thread{
    BluetoothSocket socket;
    InputStream inputStream;
    OutputStream outputStream;
    byte[] buffer;
    int bytes;
    ReceiveBluetooth(BluetoothSocket socket){
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
                bytes = inputStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }    }
        public void write(byte[] data){
            try {
                outputStream.write(data,0, data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
