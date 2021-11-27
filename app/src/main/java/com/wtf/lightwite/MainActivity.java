package com.wtf.lightwite;

import static com.wtf.lightwite.ConstantsForApp.Constants.LOGTAG;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_CLOSED;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_CONNECTED;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_CONNECTING;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_CONNECTION_FAILED;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_DISCOVERING;
import static com.wtf.lightwite.ConstantsForApp.Constants.STATE_MESSAGE_RECIVED;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.wtf.lightwite.Fragments.Bluetooth_devices_frag;
import com.wtf.lightwite.Threads.ReceiveBluetooth;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
TextView tempreature_disp;
    static TextView dev_status;
SwitchMaterial fan_switch,bulb1_switch,bulb2_switch;
Context context;
BluetoothAdapter bluetoothAdapter;
CardView fragmentContainerView;
private int REQUEST_FINE_LOCATION_PERMSN = 101;
boolean isFragmentActive =false;
public  int RESULT_DEVICE_SCAN=102;
public static boolean connectedtoBt = false;
static BluetoothDevice connectedDev ;
Intent it;
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
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_bluetooth:
                if(connectedtoBt){}
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
        fragmentContainerView = findViewById(R.id.fragment_container_view_tag);
        fragmentContainerView.setVisibility(View.GONE);
        SwitchInitialise();
        it = getIntent();
        intentExist();
    }

    private void intentExist() {
        if(it!=null){
       connectedDev =  it.getParcelableExtra("BlueTooth_Device");
       if(connectedDev!=null){
           connectedtoBt = true;
           Log.e(LOGTAG,"Device Clicked "+connectedDev.getName());
       }
       }
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
        fan_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(fan_switch.isChecked()){
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
    public  static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            BluetoothDevice btdev;
            switch (msg.what){
                case STATE_DISCOVERING:
                    break;
                case STATE_CONNECTING:
                    btdev = (BluetoothDevice) msg.obj;
                    dev_status.setText("Connecting to "+btdev.getName());
                    connectedDev = btdev;
                    break;
                case STATE_CONNECTION_FAILED:
                    btdev = (BluetoothDevice) msg.obj;
                    dev_status.setText("Couldn't Connect to  "+btdev.getName());
                    connectedDev = null;
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
                    messageRecived((byte[]) msg.obj);
                    break;
            }
            return true;
        }
    });

    private static void messageRecived(byte[] data) {
        //task to do when message Recieved
    }
}