package bettasleep.monica.com.bettasleep;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import static android.app.PendingIntent.getActivity;

public class BleConnect extends AppCompatActivity {
    public static final UUID ID_ECG = UUID.fromString("92F4B880-31B5-11E3-9C7D-0002A5D5C51B");
    public static final UUID ID_CHAR = UUID.fromString("C7BC60E0-31B5-11E3-9389-0002A5D5C51B");
    private BluetoothGatt mBluetoothGatt;
    public String current_data = "Nothing Yet";
    private TextView connection_name;
    private TextView data_preview;
    private BluetoothDevice device;
    //private BluetoothLeService mBluetoothLeSurvice;
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("gatt", "Connecting");
                mBluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("gatt", "Disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("gatt", "Discovered");
            List<BluetoothGattService> gattServices = gatt.getServices();
            BluetoothGattService service = null;
            for (int i = 0; i < gattServices.size(); i++) {
                Log.d("gatt", "GATT UUID " + gattServices.get(i).getUuid());
                if (gattServices.get(i).getUuid().equals(ID_ECG)) {
                    Log.d("gatt", "FOUND ID_ECG at " + i);
                    service = gattServices.get(i);
                }
            }
            if (service == null) {
                Log.e("gatt", "NOT FOUND: ID_ECG. Can't proceed.");
                return;
            }

            BluetoothGattCharacteristic characteristic = null;
            for (BluetoothGattCharacteristic charac : service.getCharacteristics()) {
                Log.d("chara", "UUID " + charac.getUuid());
                if (charac.getUuid().equals(ID_CHAR)) {
                    Log.e("gatt", "FOUND ID_CHAR.");
                    characteristic = charac;
                }
            }
            if (characteristic == null) {
                Log.e("chara", "NOT FOUND: ID_CHAR. Can't proceed.");
                return;
            }
            Log.d("gatt", "Enabling notifications! Hooray!");
            gatt.setCharacteristicNotification(characteristic, true);
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);

        }

        @Override
        // Characteristic notification
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            final char[] hexidecimal = "0123456789ABCDEF".toCharArray();
            //Log.d("chara", "Value changed: ");
            byte[] data = characteristic.getValue();
            /*char[] hex = new char[data.length*2];
            for (int i = 0; i < data.length; i++) {
                hex[i*2] = hexidecimal[data[i]>>>4];
                hex[i*2+1] = hexidecimal[data[i] & 0xF];
            }*/

            StringBuffer result = new StringBuffer("Packet: ");
            for (byte b : data)
                result.append(b + " ");


            current_data = result.toString();
            mHandler.post(updateData);

            //Log.d("chara", result.toString());

        }
    };

    private Runnable updateData = new Runnable() {
        @Override
        public void run() {
            data_preview.setText(current_data);
        }
    };

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data_preview = (TextView) findViewById(R.id.data_preview);
        connection_name = (TextView) findViewById(R.id.device_name);
        device = (BluetoothDevice) getIntent().getExtras().getParcelable(BleScanner.DEVICE_CONNECTION);

        mBluetoothGatt = device.connectGatt(this, false, gattCallback);

        connection_name.setText(device.getName());
        data_preview.setText(current_data);


    }
}