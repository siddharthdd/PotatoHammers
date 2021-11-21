package com.wtf.lightwite.Fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wtf.lightwite.Adapters.MyCustomRecyclerAdapter;
import com.wtf.lightwite.Adapters.ScanDevsAdapter;
import com.wtf.lightwite.R;

import java.util.ArrayList;
import java.util.Set;


public class Bluetooth_devices_frag extends Fragment {
MyCustomRecyclerAdapter adapter;
    Set<BluetoothDevice> list;
    RecyclerView recyclerView,recyclerView1;
    ProgressBar progressScanDevices;
    private BluetoothAdapter bluetoothAdapter;
    ArrayList<BluetoothDevice> newDevices;
    ScanDevsAdapter adapterAvailableDevices;

    public Bluetooth_devices_frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newDevices = new ArrayList<BluetoothDevice>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        View view = inflater.inflate(R.layout.fragment_bluetooth_devices_frag, container, false);
        recyclerView = view.findViewById(R.id.recycler_deviceslist);
        recyclerView1 = view.findViewById(R.id.recycler_deviceslist1);
        progressScanDevices = view.findViewById(R.id.scan_dev_progress);
        list = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        adapter = new MyCustomRecyclerAdapter(list, this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView1.setAdapter(adapterAvailableDevices);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this.getContext()));
        broadcastRegister();
        scanDevices();
        adapterAvailableDevices = new ScanDevsAdapter(newDevices,getContext());
        return view;
    }

    private void broadcastRegister() {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(btDevListener,intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(btDevListener,intentFilter1);

    }

    void scanDevices(){
        Toast.makeText(getContext(), "Scan Started", Toast.LENGTH_SHORT).show();
        if(bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
        progressScanDevices.setVisibility(View.VISIBLE);
        if(bluetoothAdapter.isDiscovering())
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
    }

    private BroadcastReceiver btDevListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                    newDevices.add(device);
                    adapterAvailableDevices.notifyAll();
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


}