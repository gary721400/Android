package com.example.normalbluetoothdemo.UI;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afaya.toolslib.LogUtil;
import com.afaya.toolslib.OTHERTOOLS;
import com.example.normalbluetoothdemo.R;

import java.util.ArrayList;

//import static com.afaya.bletest.SOMESTRING.BLE;


public class BlueToothBleChoiceActivity extends AppCompatActivity {

    //    private static final String TAG = "BTBleChoiceActivity";
    private static final String TAG = "BlueToothBleChoiceActiv";
    private static final int REQUEST_ENABLE_BT = 1;
    // 10秒后停止查找搜索.
    private static final long SCAN_PERIOD = 10000;
    private Button bt1;
    //    private Set<BluetoothDevice> pairedDevices = null;
    private ListView lv1 = null;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;
    private boolean mScanning;
    private Handler mHandler;
    private Button bt2;
    private String devName;
    private String devAddress;
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    if(device.getBondState() == BluetoothDevice.BOND_BONDED){
//                        Log.d(DeviceControlActivity.TAG, "BOND");
//                        final Intent intent = new Intent(DeviceScanActivity.this, DeviceControlActivity.class);
//                        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//                        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//                        if (mScanning) {
//                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                            mScanning = false;
//                        }
//                        startActivity(intent);
//                    }else{
//                        Log.d(DeviceControlActivity.TAG, "NOBOND");
//                        mLeDeviceListAdapter.addDevice(device);
//                        mLeDeviceListAdapter.notifyDataSetChanged();
//                    }
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        setContentView(R.la);
        setContentView(R.layout.ble_choice);
//        getSupportActionBar().hide();
        mHandler = new Handler();
        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
//        if (BLE) {
//            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//                Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
//                //            finish();
//                backPrevActivity();
//            }
//        }

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
            backPrevActivity();
//            finish();
//            return;
        }


//        BA = BluetoothAdapter.getDefaultAdapter();
//        LinearLayout l1 = (LinearLayout) findViewById(R.layout.choice_title);

//        InitScanBlueTooth();
//        lv1 = (ListView) findViewById(R.id.listViewScan);
        //OpenBlueTooth();
//        mBluetoothAdapter.enable();
//        StartBleReceiver();
//        if(mBluetoothAdapter.isEnabled())mBluetoothAdapter.startDiscovery();

//        bt2 = (Button) findViewById(R.id.bletitle).findViewById(R.id.title_button01);
        bt2 = (Button) findViewById(R.id.button1);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPrevActivity();
            }
        });

//        bt1 = (Button) findViewById(R.id.bletitle).findViewById(R.id.title_button02);
        bt1 = (Button) findViewById(R.id.button2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mScanning) {
                    mLeDeviceListAdapter.clear();
                    scanLeDevice(true);
                }

            }
        });

        lv1 = (ListView) findViewById(R.id.listView2);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LogUtil.E(TAG, "onItemClickPosition =  " + position);
//                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
//                final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(String.valueOf(position));
                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                if (device == null) return;
//                Intent intent = new Intent(BlueToothBleChoiceActivity.this, TestActivity.class);
//                Intent intent = new Intent();
//                intent.putExtra(SOMESTRING.EXTRAS_DEVICE_NAME, device.getName());
//                intent.putExtra(SOMESTRING.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//                AfayaApp.saveBlueToothNews(device.getName(),device.getAddress());
                devName = device.getName();
                devAddress = device.getAddress();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(device.getName()).append(" ").append(device.getAddress());
                Toast.makeText(BlueToothBleChoiceActivity.this, stringBuilder, Toast.LENGTH_SHORT).show();
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
//                startActivity(intent);
//                setResult(RESULT_OK,intent);
//                finish();
                backPrevActivity();
//                BlueToothBleChoiceActivity.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        List<BluetoothDevice> mLeDeviceList = new ArrayList<>();
//        mLeDeviceListAdapter = new LeDeviceListAdapter(this,mLeDeviceList);
//
//        lv1.setAdapter(mLeDeviceListAdapter);
////        lv1.setAdapter(mLeDeviceListAdapter);
//        setListAdapter(mLeDeviceListAdapter);
//        if (!mBluetoothAdapter.isEnabled()) {
////            scanLeDevice(true);
//            startBleReceiver();
//        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        super.onDestroy();
    }

    private void backPrevActivity() {
        saveBlueToothNews();
        Intent intent = new Intent(BlueToothBleChoiceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bt1.setEnabled(true);
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    // invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bt1.setEnabled(false);
            mBluetoothAdapter.startLeScan(mLeScanCallback);

        } else {
            mScanning = false;
            bt1.setEnabled(true);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        // invalidateOptionsMenu();
    }

    public void saveBlueToothNews() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        if (devAddress == null) return;
        if (devName == null) return;
        SharedPreferences.Editor editor = getSharedPreferences("bluetoothdata", MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("name", devName);
        editor.putString("address", devAddress);
        editor.putBoolean("ble", false);
//        editor.putLong("allNum", AfayaApp.allNum);
        editor.commit();
        LogUtil.E(TAG, "saveBlueToothNews ok!");
    }

    private void CloseBlueTooth() {
//        mBluetoothAdapter.disable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == MainActivity.CUSTUM_CODE && resultCode == Activity.RESULT_CANCELED) {
//            finish();
            backPrevActivity();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
        super.onPause();
    }

    @Override
    protected void onResume() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
               // Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
               // startActivityForResult(enableBtIntent, MainActivity.CUSTUM_CODE);
                finish();
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        // setListAdapter(mLeDeviceListAdapter);
        lv1.setAdapter(mLeDeviceListAdapter);
//        scanLeDevice(true);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        super.onRestart();
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = BlueToothBleChoiceActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listscan_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
            } else {
                viewHolder.deviceName.setText("Unknown device");
            }
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }
}


