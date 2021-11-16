package com.wtf.lightwite.Fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wtf.lightwite.Adapters.MyCustomRecyclerAdapter;
import com.wtf.lightwite.R;

import java.util.Set;


public class Bluetooth_devices_frag extends Fragment {
MyCustomRecyclerAdapter adapter;
    Set<BluetoothDevice> list;
    RecyclerView recyclerView;

    public Bluetooth_devices_frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bluetooth_devices_frag, container, false);
        recyclerView = view.findViewById(R.id.recycler_deviceslist);
        list = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        adapter = new MyCustomRecyclerAdapter(list, this.getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }


}