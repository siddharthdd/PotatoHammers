package com.wtf.lightwite.Adapters;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wtf.lightwite.ConstantsForApp.Constants;
import com.wtf.lightwite.R;

import java.util.ArrayList;
import java.util.Set;

public class MyCustomRecyclerAdapter extends RecyclerView.Adapter<MyCustomRecyclerAdapter.viewHolder> implements Constants {
    Set<BluetoothDevice> list;
    ArrayList<BluetoothDevice> btList;
    Context context;
    ImageView icon;
    TextView nameofDevice;
    TextView macAddress;
    OnPairedListener mOnPairedListener;

    public MyCustomRecyclerAdapter(Set<BluetoothDevice> list,Context context,OnPairedListener mOnPairedListener){
        this.list = list;
        this.context = context;
        this.mOnPairedListener = mOnPairedListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bt_devices_list,parent,false);
        list = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        return new viewHolder(view,mOnPairedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
    BluetoothDevice device  = new ArrayList<BluetoothDevice>(list).get(position);
    nameofDevice.setText(device.getName());
    macAddress.setText(device.getAddress());
        //Set the Text and Shit
        int Btclass = device.getBluetoothClass().getDeviceClass();
        if(Btclass== BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES || Btclass == BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET){
            icon.setImageResource(R.drawable.headfone1_w);
        }
        else if(Btclass == BluetoothClass.Device.PHONE_SMART || Btclass == BluetoothClass.Device.PHONE_UNCATEGORIZED)
            icon.setImageResource(R.drawable.phone1_w);
        else if(Btclass == BluetoothClass.Device.COMPUTER_DESKTOP || Btclass == BluetoothClass.Device.COMPUTER_LAPTOP || Btclass == BluetoothClass.Device.COMPUTER_SERVER)
            icon.setImageResource(R.drawable.top1_w);
        else {
            icon.setImageResource(android.R.drawable.stat_sys_data_bluetooth);
        }
        darkMode();

    }
    public boolean darkMode(){
        Resources resources = context.getResources();
        if(resources.getString(R.string.mode).equals("Day"))
        {
            icon.setColorFilter(Color.argb(255,0,0,0));
            icon.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,0,0)));
            return false;
        }
        if(resources.getString(R.string.mode).equals("Night"))
        {
            icon.setColorFilter(Color.argb(255,0,0,0));
            return  true;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{
        MyCustomRecyclerAdapter.OnPairedListener onPairedListener;
        public viewHolder(@NonNull View itemView, MyCustomRecyclerAdapter.OnPairedListener onPairedListener) {
            super(itemView);
            icon = itemView.findViewById(R.id.device_type_icon);
            nameofDevice = itemView.findViewById(R.id.name_of_device_textview);
            macAddress = itemView.findViewById(R.id.Mac_id_textview);
            this.onPairedListener = onPairedListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            btList =  new ArrayList<BluetoothDevice>(list);
            onPairedListener.onPairedClick(getAdapterPosition(),btList.get(getAdapterPosition()));
        }
    }
    public interface OnPairedListener{
        void onPairedClick(int position,BluetoothDevice bt);
    }
}
