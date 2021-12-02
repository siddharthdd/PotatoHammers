package com.wtf.lightwite.Adapters;
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

public class ScanDevsAdapter extends RecyclerView.Adapter<ScanDevsAdapter.viewHolder> {
    ArrayList<BluetoothDevice> list;
    Context context;
    ImageView icon;
    TextView nameofDevice;
    TextView macAddress;
    OnNoteListener mOnNoteListener;
    public ScanDevsAdapter(ArrayList<BluetoothDevice> list,Context context,OnNoteListener onNoteListener){
        this.list = list;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ScanDevsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bt_devices_list,parent,false);
        return new ScanDevsAdapter.viewHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanDevsAdapter.viewHolder holder, int position) {
        BluetoothDevice device  = list.get(position);
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class viewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{
        OnNoteListener onNoteListener;
        public viewHolder(@NonNull View itemView,OnNoteListener onNoteListener) {
            super(itemView);
            icon = itemView.findViewById(R.id.device_type_icon);
            nameofDevice = itemView.findViewById(R.id.name_of_device_textview);
            macAddress = itemView.findViewById(R.id.Mac_id_textview);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        onNoteListener.onNewClick(getAdapterPosition(),list.get(getAdapterPosition()));
        }
    }
    public interface OnNoteListener{
        void onNewClick(int position, BluetoothDevice bt);

    }
}
