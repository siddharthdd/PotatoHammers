package com.wtf.lightwite.Fragments;

import static com.wtf.lightwite.ConstantsForApp.Constants.LOGTAG;

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
import com.wtf.lightwite.Threads.ConnectAsClient;

import java.lang.reflect.Method;
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
    ConnectAsClient connectAsClient;

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
                Log.e(LOGTAG,"new dev found "+device.getName());
                if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                    if(newDevices.contains(device)){
                        Log.e(LOGTAG, "onReceive: New DEV");
                    }
                    else{newDevices.add(device);
                    adapterAvailableDevices.notifyDataSetChanged();
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
    }
    void initialiseFields(){
        newDevices = new ArrayList<BluetoothDevice>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        adapterAvailableDevices = new ScanDevsAdapter(newDevices,getContext(),this);
        pairedList = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        pairedAdapter = new MyCustomRecyclerAdapter(pairedList, this.getContext(),this);
    }
    private void setAdapterforPaired() {
        recyclerViewPaired.setLayoutManager( new LinearLayoutManager(this.getContext()));
        recyclerViewPaired.setAdapter(pairedAdapter);
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
        Toast.makeText(getContext(), "Scan Started", Toast.LENGTH_SHORT).show();
    }
    void scanDevFit(){
    recyclerViewAvailable.setLayoutManager(new LinearLayoutManager(this.getContext()));
    recyclerViewAvailable.setAdapter(adapterAvailableDevices);

}

    @Override
    public void onNewClick(int position, BluetoothDevice bt) {
        try {
            boolean tempboll  = createBond(bt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectAsClient = new ConnectAsClient(bt);
        connectAsClient.start();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("BlueTooth_Device",bt);
        startActivity(intent);
    }

    @Override
    public void onPairedClick(int position,BluetoothDevice bt) {
        MainActivity.connectedtoBt = false;
        connectAsClient = new ConnectAsClient(bt);
        connectAsClient.start();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("BlueTooth_Device",bt);
        startActivity(intent);
    }

    public boolean createBond(BluetoothDevice btDevice)
            throws Exception
    { Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }
}