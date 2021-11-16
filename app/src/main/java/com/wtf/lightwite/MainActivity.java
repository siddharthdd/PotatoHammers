package com.wtf.lightwite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.wtf.lightwite.Fragments.Bluetooth_devices_frag;

public class MainActivity extends AppCompatActivity {
double tempreature;
TextView tempreature_disp;
SwitchMaterial fan_switch,bulb1_switch,bulb2_switch;
Context context;
BluetoothAdapter bluetoothAdapter;
CardView fragmentContainerView;
private int REQUEST_FINE_LOCATION_PERMSN = 101;
boolean isFragmentActive =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context =this;
        tempreature_disp = findViewById(R.id.switch1);
        fan_switch = findViewById(R.id.switch2);
        bulb1_switch = findViewById(R.id.switch5);
        bulb2_switch = findViewById(R.id.switch6);
        fragmentContainerView = findViewById(R.id.fragment_container_view_tag);
        fragmentContainerView.setVisibility(View.GONE);
        SwitchInitialise();
        initBt();
    }
    void checkPermissions(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION_PERMSN);}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_FINE_LOCATION_PERMSN){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }
            else{
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Permission Denied App Won't Work", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPermissions();
                            }
                        })
                        .setMessage("Please Grant Permission of Location")
                        .setIcon(R.drawable.ic_launcher_foreground)
                        .show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void initBt(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(context,"Bluetooth isn't Supported on this device",Toast.LENGTH_SHORT).show();
        }
        else{
            if(bluetoothAdapter.isEnabled()) {Toast.makeText(context, "Bluetooth Already Enabled", Toast.LENGTH_SHORT).show();
            }
            else bluetoothAdapter.enable();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_bluetooth:
                Toast.makeText(context, "Clicked bluetooth button", Toast.LENGTH_SHORT).show();
                initBt();
                return true;
            case R.id.menu_search_devices:
                checkPermissions();
                Toast.makeText(context, "Clicked Search button", Toast.LENGTH_SHORT).show();
                showFrag();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showFrag() {
        Bluetooth_devices_frag bluetooth_devices_frag = new Bluetooth_devices_frag();
        fragmentContainerView.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view_tag,bluetooth_devices_frag);
        fragmentTransaction.commit();
        isFragmentActive=true;

    }

    void SwitchInitialise(){
        fan_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(fan_switch.isChecked()){
                    //When Fan Switch is On
                    Toast.makeText(MainActivity.this, "Fan Switch ON", Toast.LENGTH_SHORT).show();
                }
                else if(!fan_switch.isChecked()){
                    //When Fan Switch is Off
                    Toast.makeText(MainActivity.this, "Fan Switch OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bulb1_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bulb1_switch.isChecked()){
                    //When Bulb 1 is On
                    Toast.makeText(MainActivity.this, "Bulb 1 Switch ON", Toast.LENGTH_SHORT).show();
                }
                else if(!bulb1_switch.isChecked()){
                    //When Bulb 1 is Off
                    Toast.makeText(MainActivity.this, "Bulb 1 Switch OFF", Toast.LENGTH_SHORT).show();

                }
            }
        });
        bulb2_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bulb2_switch.isChecked()){
                    //When Bulb 2 is On
                    Toast.makeText(MainActivity.this, "Bulb 2 Switch ON", Toast.LENGTH_SHORT).show();
                }
                else if(!bulb2_switch.isChecked()){
                    //When Bulb 2 is Off
                    Toast.makeText(MainActivity.this, "Bulb 2 Switch OFF", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!isFragmentActive){
            new AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_baseline_close)
                    .setTitle("Close The App")
                    .setMessage("Are You Sure You Want to Exit The App?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(context, "App Closing Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();

        }
    else{
        fragmentContainerView.setVisibility(View.GONE);
        isFragmentActive=false;
        }}
}