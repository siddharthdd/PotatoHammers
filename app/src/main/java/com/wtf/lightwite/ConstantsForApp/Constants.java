package com.wtf.lightwite.ConstantsForApp;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class Constants {
    public static String UUID_STRING = "JAVA_MINI_PROJECT";
    public static final int STATE_DISCOVERING = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTION_FAILED = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CLOSED = 4;
    public  static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_DISCOVERING:
                    break;
                case STATE_CONNECTING:
                    break;
                case STATE_CONNECTION_FAILED:
                    break;
                case STATE_CONNECTED:
                    break;
                case STATE_CLOSED:
                    break;
            }
            return true;
        }
    });

}
