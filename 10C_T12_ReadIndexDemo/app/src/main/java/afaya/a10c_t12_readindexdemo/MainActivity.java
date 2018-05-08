package afaya.a10c_t12_readindexdemo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    Button conButton;
    Button getScreenBtn;
    TextView textViewState;
    TextView textViewIndex;
    public Context applicationContext;
    UsbManager manager;
    HashMap<String, UsbDevice> deviceList;
    UsbInterface mInterface;
    boolean hasDevice = false;
    UsbDevice findDevice = null;
    Iterator<UsbDevice> deviceIterator=null;
    public boolean readIndex = false;

    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            Log.e("main","what = "+msg.what+" handlemessage = "+msg.arg2);
            switch(msg.what)
            {
                case 1:
                    textViewIndex.setText(""+msg.arg2);
                    break;
                case 2:
                    textViewState.setText("connected");
                    break;
                default:
                    break;
            }
            textViewIndex.invalidate();
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationContext = getApplicationContext();

        conButton = (Button)findViewById(R.id.connectButton);
        getScreenBtn = (Button)findViewById(R.id.getScreenButton);
        textViewState = (TextView)findViewById(R.id.textState);
        textViewIndex = (TextView)findViewById(R.id.textIndex);
    }
    final Thread threadReadIndex = new Thread(new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 2;
            mHandler.sendMessage(message);
            Log.e("Main","send2 "+mHandler);
            UsbDeviceConnection connection = manager.openDevice(findDevice);
            Log.e("Main","connection = "+connection);
            if(connection==null)
            {
                return;
            }
            connection.claimInterface(mInterface,  true );
            byte [] inputbuf =  new   byte [ 64 ];
            byte bindcmd[] = {(byte) 0xFE, (byte) 0xCA, (byte) 0xCA, (byte) 0xFE, 0x03, 0x01};
            Log.e("Main","start read");
            while(readIndex) {

                int b = connection.controlTransfer(0x21, 9, 200, 0, bindcmd, bindcmd.length, 200);
                //int b = connection.controlTransfer(0x21, 9, 200, 0, inputbuf, inputbuf.length, 200);
                b = connection.controlTransfer(0xa1, 1, 256, 0, inputbuf, inputbuf.length, 200);

                if (inputbuf[12] == 1) {
                    int a1 = 0xFF&inputbuf[14];
                    int a2 = 0xFF&inputbuf[15];
                    int index = a2<<8|a1;
                    message = new Message();
                    message.what = 1;
                    message.arg1 = 1;
                    message.arg2 = index;
                    mHandler.sendMessage(message);
                    Log.e("main","index = "+index+" handler = "+mHandler);
                }
            }
        }
    });
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        threadReadIndex.start();
                    } else {
                        finish();
                    }
                }
            }
        }
    };
    final Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            hasDevice = false;
            UsbDevice device=null;
            Log.e("main","get usb service");
            manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            deviceList = manager.getDeviceList();
            deviceIterator = deviceList.values().iterator();
            Log.e("main","device count = "+deviceList.size());
            while (deviceIterator.hasNext()) {
                device = deviceIterator.next();
                Log.e("main ","device = "+device);
                Log.e("main ","device vid = "+device.getVendorId());
                Log.e("main ","device pid = "+device.getProductId());
                Log.e("main ","device interfaceCount = "+device.getInterfaceCount());
                if(device.getProductId() != 1728&&device.getProductId() != 255)continue;
                for (int j = 0; j < device.getInterfaceCount(); j++) {
                    mInterface = device.getInterface(j);
                    if (mInterface.getInterfaceClass() == 3
                            && mInterface.getInterfaceSubclass() == 0
                            &&( mInterface.getInterfaceProtocol() == 0
                            || mInterface.getInterfaceProtocol() == 1)
                            ) {
                        hasDevice = true;
                        findDevice = device;
                        if(!manager.hasPermission(device)) {
                            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(applicationContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
                            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                            registerReceiver(mUsbReceiver, filter);
                            manager.requestPermission(device, mPermissionIntent);
                        }
                        else
                        {
                            threadReadIndex.start();
                        }
                    }
                }
            }
        }
    });
    @Override
    protected void onResume() {
        conButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("main ","thisHandler = "+mHandler);
                readIndex = true;
                if(!thread.isAlive())
                {
                    thread.start();
                }
            }
        });
        getScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                String strResolution = "Resolution: " + dm.widthPixels + "*" + dm.heightPixels;
                Log.e("MainActivity"," "+strResolution);
                /*
                Display display = getWindowManager().getDefaultDisplay();
                Log.e("MainActivity","width = "+display.getWidth());
                Log.e("MainActivity","height = "+display.getHeight());*/
            }
        });
        super.onResume();
    }

}