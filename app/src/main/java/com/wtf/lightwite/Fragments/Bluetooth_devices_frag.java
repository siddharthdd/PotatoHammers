package com.wtf.lightwite.Fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wtf.lightwite.Adapters.MyCustomRecyclerAdapter;
import com.wtf.lightwite.Adapters.ScanDevsAdapter;
import com.wtf.lightwite.MainActivity;
import com.wtf.lightwite.R;

import java.util.ArrayList;
import java.util.Set;

public class Bluetooth_devices_frag extends Fragment implements ScanDevsAdapter.OnNoteListener,MyCustomRecyclerAdapter.OnPairedListener {
    MyCustomRecyclerAdapter pairedAdapter;
    Set<BluetoothDevice> pairedList;
    RecyclerView recyclerViewPaired, recyclerViewAvailable;
    ProgressBar progressScanDevices;
    private BluetoothAdapter bluetoothAdapter;
    ArrayList<BluetoothDevice> newDevices;
    ScanDevsAdapter adapterAvailableDevices;
    public static int REQCODE_BTDEV = 1101;

    public Bluetooth_devices_frag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth_devices_frag, container, false);
        initialiseUI(view);
        initialiseFields();
        setAdapterforPaired();
        broadcastRegister();
        scanDevices();
        scanDevFit();
        return view;
    }

    private BroadcastReceiver btDevListener = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                    if(newDevices.contains(device)){}
                    else{newDevices.add(device);
                    }
                }
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                progressScanDevices.setVisibility(View.GONE);
                if(newDevices.isEmpty()){
                    Toast.makeText(context, "No new device Found", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Click on the device to Chat", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    void initialiseUI(View view){
        recyclerViewPaired = view.findViewById(R.id.recycler_deviceslist);
        recyclerViewAvailable = view.findViewById(R.id.recycler_deviceslist1);
        progressScanDevices = view.findViewById(R.id.scan_dev_progress);
        progressScanDevices.setVisibility(View.VISIBLE);
    }
    void initialiseFields(){
        newDevices = new ArrayList<BluetoothDevice>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        adapterAvailableDevices = new ScanDevsAdapter(newDevices,getContext(),this);
        pairedList = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        pairedAdapter = new MyCustomRecyclerAdapter(pairedList, this.getContext(),this);
    }
    private void setAdapterforPaired() {
        recyclerViewPaired.setAdapter(pairedAdapter);
        recyclerViewPaired.setLayoutManager( new LinearLayoutManager(this.getContext()));
    }
    private void broadcastRegister() {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(btDevListener,intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(btDevListener,intentFilter1);

    }
    void scanDevices(){
        if(bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
        progressScanDevices.setVisibility(View.VISIBLE);
        if(bluetoothAdapter.isDiscovering())
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
        Toast.makeText(getContext(), "Scan Started", Toast.LENGTH_SHORT).show();
    }
    void scanDevFit(){
    recyclerViewAvailable.setAdapter(adapterAvailableDevices);
    recyclerViewAvailable.setLayoutManager(new LinearLayoutManager(this.getContext()));

}

    @Override
    public void onNoteClick(int position,BluetoothDevice bt) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("BlueTooth_Device",bt);
        startActivity(intent);
    }

    @Override
    public void onPairedClick(int position,BluetoothDevice bt) {
        Log.e("CLICK","U CLICKEDDD PAIRED "+bt.getAddress());
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("BlueTooth_Device",bt);
        startActivity(intent);
    }
}