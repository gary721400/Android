package afaya.a10c_t12_readindexdemo;

import java.util.Arrays;


import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncIndexGetTask extends AsyncTask<Object, Object, Object> {

	String TAG = "WRITE_ORDER_TASK";
	Object[] list_logininput;
	Object[] list_orderinput;
	Object[] list_result;
	String Oi_OdNumber;
	
	static AsyncIndexGetTask instance;
	UsbDevice mUsbDevice;
	UsbDeviceConnection connection;
	UsbEndpoint ep;
	Handler hr;
	public byte[] buf = new byte[64];
	private int readdata(byte[] inputbuf)
	{
		boolean b_no_data=true;
		int b = 0;
		if(connection != null && ep != null)
		{
			Arrays.fill(inputbuf, (byte) 0);
			byte bindcmd[] = {(byte) 0xFE, (byte) 0xCA, (byte) 0xCA, (byte) 0xFE, 0x03, 0x01};
			b = connection.controlTransfer(0x21, 9, 200, 0, bindcmd, bindcmd.length, 200);
			//b = connection.controlTransfer(0x21, 9, 200, 0, inputbuf, inputbuf.length, 200);
			b = connection.controlTransfer(0xa1, 1, 256, 0, inputbuf, inputbuf.length, 200);
			

				if(inputbuf[12] != 0)
				{
					b_no_data = false;
				}
				if(b_no_data)
					return 0;
				else
					return b;
		}
		return 0;
	}
	
	public static AsyncIndexGetTask getInstance(UsbDevice mUsbDevice, UsbDeviceConnection connection, UsbEndpoint ep,Handler hr)
	{
		if (instance == null) {
			instance = new AsyncIndexGetTask(mUsbDevice,connection,ep,hr);			
		}
		else
		{
			instance.mUsbDevice = mUsbDevice;
			instance.ep = ep;
			instance.connection = connection;
			instance.hr = hr;
		}
		return instance;
	}
	
	 public AsyncIndexGetTask(UsbDevice mUsbDevice, UsbDeviceConnection connection, UsbEndpoint ep,Handler hr){
		 this.mUsbDevice = mUsbDevice;
		 this.ep = ep;
		 this.connection = connection;
		 this.hr = hr;
	     
	 }
	 
    protected Void doInBackground(Object... Objects) {
   	 boolean isUsbThreadRunning = true;
   	 boolean isPollingGetIndex = true;
   	int b1,b2,b3,b4;
		while(isUsbThreadRunning)
		{	
			
			if(isPollingGetIndex)
			{
				Arrays.fill(buf, (byte) 0);
				if(true){
	            try
	            {
					int b = readdata(buf);
					if (b >= 16) {

						//b1 = buf[14]&0x0FF;
						//b2 = buf[15]&0x0FF;
						int packettype = 0;//b1<<8|b2;

						b1 = buf[14]&0x0FF;
						b2 = buf[15]&0x0FF;
						//b3 = buf[22]&0x0FF;
						//b4 = buf[23]&0x0FF;
						int index = b1<<8|b2;//
						if(packettype==0 && index ==0) //maybe bug when index=0, type=0, set packtype to 129 fix
							packettype = 129;
						Message msg = new Message();
						msg.obj = new USBpacket(packettype, index);
						
						instance.hr.sendMessage(msg);
							//Log.e("index", String.valueOf(index));


					}
	            }
	            catch(Exception e) {
	                e.printStackTrace();
	            } 
				}
		}
	}
		return null;
    }

    protected void onProgressUpdate(Object... Objects) {

    }

    protected void onPostExecute(Object result) {
   	// pd.dismiss();
   	 
   	 
    }
}
