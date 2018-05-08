package afaya.a10c_t12_readindexdemo;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.util.Log;


public class GetIndex {
	final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private static GetIndex instance;
	OnUsbPermissionVerify listener;


	public static final int MESSAGE_LOG = 1;
	public static final int MESSAGE_DEVICE_ONLINE = 2;

	public UsbDevice _device= null;
	public UsbEndpoint _ep= null;
	public UsbDeviceConnection _connection= null;
	public UsbInterface _usbinterface = null;
	public boolean b_pendingintent_already_send = false;


	public final int A30U_OTHER = 1;
	public final int A30U_TYPE_ENCRYPTDOG_V1 = 2;
	
	private int a30u_type = 0;
	private Context mContext;
	UsbManager mUsbManager;
	
	private GetIndex(Context mContext,OnUsbPermissionVerify listener)
	{
		this.mContext = mContext;
		this.listener = listener;

	}
	public static GetIndex getInstance(Context mContext,OnUsbPermissionVerify listener)
	{
		if (instance == null) {
			instance = new GetIndex(mContext,listener);			
		}
		return instance;
	}
	
	public boolean IsUSBReady()
	{
		return (_device!=null && _ep != null && _connection!=null);
	}

	public void uninit()
	{
		_device= null;
		_ep= null;
		if(_connection!=null)
		{
			_connection.releaseInterface(_usbinterface);	
			_connection.close();
			_connection= null;
		}
		if(mContext!=null && mUsbReceiver!=null)
		{
			try
			{
				mContext.unregisterReceiver(mUsbReceiver);
			}catch(IllegalArgumentException e) { }
		}
	}
	
	public void getIndexFromPen()
	{
		UsbDevice device;
		
		mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
		_device =null;
		_ep= null;
		if(_connection!=null)
		{
			_connection.releaseInterface(_usbinterface);	
			_connection.close();
			_connection= null;
		}
		device = FindAfayaDevice();

		GetConnectInfo(device);

	}

	public int get30Utype()
	{
		return a30u_type;
	}
	
	private boolean GetConnectInfo(UsbDevice device)
	{
		UsbDeviceConnection connection;
		UsbEndpoint ep;

		if (device != null ) { //A308
			
			if(!mUsbManager .hasPermission(device) && !b_pendingintent_already_send)
			{
				PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT);
				
				IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
				mContext.registerReceiver(mUsbReceiver, filter);
				
				
				mUsbManager .requestPermission(device, pi);
				b_pendingintent_already_send = true;
			}
			else
			{
				_usbinterface = GetInterface(device);
				if(_usbinterface != null)
				{
					connection = GetConnection(device, _usbinterface);
					if(connection != null)
					{
						ep = GetEndPoint(connection, _usbinterface);
						if(ep != null)
						{
							this._device = device;
							this._connection = connection;
							this._ep = ep;
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	

	ArrayList<UsbDevice> devices ;
	private UsbDevice FindAfayaDevice()
	{
		devices = new ArrayList<UsbDevice>();
		for (UsbDevice mdevice : mUsbManager .getDeviceList().values()) {

			if(mdevice.getVendorId() == 5855)
			{
				if (mdevice.getProductId() == 255) {
					a30u_type = A30U_OTHER;
					devices.add(mdevice);
					return mdevice;				
				}				
			}
		}

		if(devices.size()>0)
			return devices.get(0);
		return null;
	}
	
	private UsbEndpoint GetEndPoint(UsbDeviceConnection connection, UsbInterface mInterface)
	{
		UsbEndpoint ep;
		if ( connection == null)
			return null;
		
		if (connection.claimInterface(mInterface, true)) {
			//DisplayToast("claim interface succeeded");
			//DisplayToast(connection.getSerial());
			for (int i = 0; i < mInterface.getEndpointCount(); i++) {
				ep = mInterface.getEndpoint(i);
				//DisplayToast("ep =" + ep);
				//DisplayToast("ep type =" + ep.getType());
				//DisplayToast("ep direction =" + ep.getDirection());
				//ep.getEndpointNumber()

				if (ep.getType() == UsbConstants.USB_CLASS_HID) {
					if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
						return ep;						
					}					
				}
			}
		} else {	
			//DisplayToast("claim interface failed");
			connection.close();
		}
		return null;		
	}
	
	private UsbInterface GetInterface(UsbDevice device)
	{
		UsbInterface mInterface = null;
        if(device != null){
			//DisplayToast("hasPermission = " + bl);
			//DisplayToast("InterfaceCount = " + device.getInterfaceCount());
			int count = device.getInterfaceCount();
			for (int j = 0; j < count; j++) {
				mInterface = device.getInterface(j);
				//DisplayToast("InterfaceClass = " + intf.getInterfaceClass());
				//DisplayToast("InterfaceSubclass = "						+ intf.getInterfaceSubclass());
				//DisplayToast("InterfaceProtocol = "						+ intf.getInterfaceProtocol());
				
				if (mInterface.getInterfaceClass() == 3
						&& mInterface.getInterfaceSubclass() == 0
						&& mInterface.getInterfaceProtocol() == 1 ) 
				{
					//t12
					return mInterface;
				}
				else if (mInterface.getInterfaceClass() == 3
						&& mInterface.getInterfaceSubclass() == 0
						&& mInterface.getInterfaceProtocol() == 0 ) 
				{
					//atmel
					return mInterface;
				}
			}
        }
		return null;
	}
	
	private UsbDeviceConnection GetConnection(UsbDevice device, UsbInterface intf)
	{
		UsbDeviceConnection connection;
		if ( intf != null) {
			connection = mUsbManager.openDevice(device);
			if (connection != null) {
				return connection;
			}
		}

       return null;
	}
		BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        if (ACTION_USB_PERMISSION.equals(action)) {
		            synchronized (this) {
		                //UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
		        		UsbDevice device = FindAfayaDevice();
		                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
		                	GetConnectInfo(device);
		                	listener.onGetResult(true, null);
		                	//GlobalFunc.SendMsgToHandler(GlobalFunc.MSG_ARG1.USB_NOTIFICATION, GlobalFunc.MSG_ARG2.USB_RECHECK_GO);
		                	
		                } 
		                else {
		                	listener.onGetResult(false, null);
		                	//GlobalFunc.SendMsgToHandler(GlobalFunc.MSG_ARG1.USB_NOTIFICATION, GlobalFunc.MSG_ARG2.USB_REJECTED);
		                    Log.e("main", "permission denied for device " + device);
		                }
		                b_pendingintent_already_send = false;
		            }
		        }
		    }
		};


}
