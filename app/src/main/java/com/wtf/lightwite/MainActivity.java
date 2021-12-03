package com.wtf.lightwite;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.wtf.lightwite.ConstantsForApp.Constants;
import com.wtf.lightwite.Fragments.Bluetooth_devices_frag;
import com.wtf.lightwite.Threads.ReceiveBluetooth;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements Constants {
static TextView tempreature_disp;
CardView tempreatureCardview;
static TextView dev_status;
SwitchMaterial fan_switch,bulb1_switch,bulb2_switch;
Context context;
BluetoothAdapter bluetoothAdapter;
CardView fragmentContainerView;
RangeSlider slider;
MaterialButtonToggleGroup modeButton;
private final int REQUEST_FINE_LOCATION_PERMSN = 101;
boolean isFragmentActive =false;
public static boolean connectedtoBt = false;
static BluetoothDevice connectedDev ;
static ReceiveBluetooth receiveBluetooth;
static BluetoothSocket btSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context =this;
        initialiseUI();
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
                            onDestroy();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        if(context.getResources().getString(R.string.mode).equals("Day")){
        menu.getItem(0).getIcon().setColorFilter(Color.argb(255,0,0,0), PorterDuff.Mode.MULTIPLY);
        menu.getItem(1).getIcon().setColorFilter(Color.argb(255,0,0,0),PorterDuff.Mode.MULTIPLY);
        }
        if(context.getResources().getString(R.string.mode).equals("Night")){
            menu.getItem(0).getIcon().setColorFilter(Color.argb(255,255,255,255), PorterDuff.Mode.MULTIPLY);
            menu.getItem(1).getIcon().setColorFilter(Color.argb(255,255,255,255),PorterDuff.Mode.MULTIPLY);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_bluetooth:
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

    void initialiseUI(){
        tempreature_disp = findViewById(R.id.switch1);
        dev_status = findViewById(R.id.device_status_text);
        fan_switch = findViewById(R.id.switch2);
        bulb1_switch = findViewById(R.id.switch5);
        bulb2_switch = findViewById(R.id.switch6);
        modeButton = findViewById(R.id.switch4);
        tempreatureCardview = findViewById(R.id.card1);
        fragmentContainerView = findViewById(R.id.fragment_container_view_tag);
        fragmentContainerView.setVisibility(View.GONE);
        slider = findViewById(R.id.switch3);
        slider.setValueFrom(0f);
        slider.setValueTo(255f);
        SwitchInitialise();

    }

    void checkPermissions(){
       if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION_PERMSN);}
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

    private void showFrag() {
        initBt();
        Bluetooth_devices_frag bluetooth_devices_frag = new Bluetooth_devices_frag();
        fragmentContainerView.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view_tag,bluetooth_devices_frag);
        fragmentTransaction.commit();
        Log.e(LOGTAG, "Fragment Shown" );
        isFragmentActive=true;
        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
        startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                .putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,500));
            Log.e(LOGTAG, "Scan Started" );
    }

    }

    void SwitchInitialise(){
        tempreatureCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSocket!=null){
                        ReceiveBluetooth rec = new ReceiveBluetooth(btSocket);
                        receiveBluetooth.write("T".getBytes());
                        try {
                            Thread.sleep(700);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        rec.start();
                    ReceiveBluetooth rec2 = new ReceiveBluetooth(btSocket);
                    receiveBluetooth.write("T".getBytes());
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rec2.start();
                   }
            }
        });
        fan_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(fan_switch.isChecked()){
                    //"C"
                    if(btSocket!=null)
                        receiveBluetooth.write("C".getBytes());
                    Toast.makeText(MainActivity.this, "Fan Switch ON", Toast.LENGTH_SHORT).show();
                }
                else if(!fan_switch.isChecked()){
                    //When Fan Switch is Off "c"
                    if(btSocket!=null)
                        receiveBluetooth.write("c".getBytes());
                    Toast.makeText(MainActivity.this, "Fan Switch OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bulb1_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bulb1_switch.isChecked()){
                    //When Bulb 1 is On "B"
                    if(btSocket!=null)
                        receiveBluetooth.write("B".getBytes());
                    Toast.makeText(MainActivity.this, "Bulb 1 Switch ON", Toast.LENGTH_SHORT).show();
                }
                else if(!bulb1_switch.isChecked()){
                    //When Bulb 1 is Off "b"
                    if(btSocket!=null)
                        receiveBluetooth.write("b".getBytes());
                    Toast.makeText(MainActivity.this, "Bulb 1 Switch OFF", Toast.LENGTH_SHORT).show();

                }
            }
        });
        bulb2_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bulb2_switch.isChecked()){
                    //When Bulb 2 is On "z"
                    if(btSocket!=null)
                        receiveBluetooth.write("z".getBytes());
                    Toast.makeText(MainActivity.this, "Bulb 2 Switch ON", Toast.LENGTH_SHORT).show();
                }
                else if(!bulb2_switch.isChecked()){
                    //When Bulb 2 is Off "a"
                    if(btSocket!=null)
                        receiveBluetooth.write("a".getBytes());
                    Toast.makeText(MainActivity.this, "Bulb 2 Switch OFF", Toast.LENGTH_SHORT).show();

                }
            }
        });


        modeButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch(checkedId){
                case R.id.btn1:
                    if(isChecked){
//                    "D"
                        if(btSocket!=null)
                            receiveBluetooth.write("D".getBytes());
                    }
                    else{
                        if(btSocket!=null)
                            receiveBluetooth.write("d".getBytes());
                    }
                    break;
                case R.id.btn2:
                    if(isChecked){
//                    "E"
                        if(btSocket!=null)
                            receiveBluetooth.write("E".getBytes());
                    }
                    else{
                        if(btSocket!=null)
                            receiveBluetooth.write("e".getBytes());
                    }
                    break;
                case R.id.btn3:
                    if(isChecked){
//                    "H"
                        if(btSocket!=null)
                            receiveBluetooth.write("H".getBytes());
                    }
                    else{
                        if(btSocket!=null)
                            receiveBluetooth.write("h".getBytes());
                    }
                    break;
                default:
                    break;
                }
            }
        });
        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                //G and thumbvlue
                if(btSocket!=null){
                    receiveBluetooth.write("G".getBytes());
                    String str = String.valueOf(value);
                    receiveBluetooth.write(str.getBytes(StandardCharsets.US_ASCII));}
            }
        });
    }
    public  static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            BluetoothDevice btdev;
            switch (msg.what){
                case STATE_DISCOVERING:
                    break;
                case STATE_CONNECTING:
                    if(btSocket!=null) {receiveBluetooth.cancel();
                    btSocket=null;}
                    btdev = (BluetoothDevice) msg.obj;
                    dev_status.setText("Connecting to "+btdev.getName());
                    connectedDev = btdev;
                    break;
                case STATE_CONNECTION_FAILED:
                    btdev = (BluetoothDevice) msg.obj;
                    dev_status.setText("Couldn't Connect to  "+btdev.getName());
                    connectedDev = null;
                    if(btSocket!=null) {receiveBluetooth.cancel();
                        btSocket=null;}
                    break;
                case STATE_CONNECTED:
                    BluetoothSocket socket = (BluetoothSocket) msg.obj;
                    dev_status.setText("Connected to "+connectedDev.getName());
                    btSocket = socket;
                    receiveBluetooth = new ReceiveBluetooth(btSocket);
                    receiveBluetooth.start();
                    connectedtoBt = true;
                    break;
                case STATE_CLOSED:
                    break;
                case STATE_MESSAGE_RECIVED:
                    tempreature_disp.setText((new String((byte[]) msg.obj)+" °C"));
                    tempreature_disp.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
    });
}