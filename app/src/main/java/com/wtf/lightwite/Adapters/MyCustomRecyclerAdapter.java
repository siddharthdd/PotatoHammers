package com.wtf.lightwite.Adapters;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wtf.lightwite.R;

import java.util.ArrayList;
import java.util.Set;

public class MyCustomRecyclerAdapter extends RecyclerView.Adapter<MyCustomRecyclerAdapter.viewHolder>{
    Set<BluetoothDevice> list;
    ArrayList<BluetoothDevice> btList;
    Context context;
    ImageView icon;
    TextView nameofDevice;
    TextView macAddress;

    public MyCustomRecyclerAdapter(Set<BluetoothDevice> list,Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bt_devices_list,parent,false);
        list = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
    BluetoothDevice device  = new ArrayList<BluetoothDevice>(list).get(position);
    nameofDevice.setText(device.getName());
    macAddress.setText(device.getAddress());
        //Set the Text and Shit
        int Btclass = device.getBluetoothClass().getDeviceClass();
        if(Btclass== BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES || Btclass == BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET){
            icon.setImageResource(android.R.drawable.checkbox_on_background);
        }
        else if(Btclass == BluetoothClass.Device.PHONE_SMART || Btclass == BluetoothClass.Device.PHONE_UNCATEGORIZED)
            icon.setImageResource(android.R.drawable.checkbox_on_background);
        else {
            icon.setImageResource(android.R.drawable.checkbox_off_background);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends  RecyclerView.ViewHolder{

        public viewHolder(@NonNull View itemView) {
            super(itemView);
        icon = itemView.findViewById(R.id.device_type_icon);
        nameofDevice = itemView.findViewById(R.id.name_of_device_textview);
        macAddress = itemView.findViewById(R.id.Mac_id_textview);
        }
    }
}
