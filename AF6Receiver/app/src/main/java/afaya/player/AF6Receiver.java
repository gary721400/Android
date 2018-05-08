package afaya.player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.R.integer;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

public class AF6Receiver implements Serializable {
	private static final double LAST_VERSION = 3.0;
	public static final int CHECK_BOOKNAME_VERSIONERROR = 3;
	public static final int CHECK_BOOKNAME_NOTEXIST = 2;
	public static final int CHECK_BOOKNAME_HANDLEISUSING = 1;
	public static final int CHECK_BOOKNAME_SUCCESS = 0;
	public static final int MEDIATYPE_MP3 = 0;
	public static final int MEDIATYPE_WMA = 1;
	public static final int MEDIATYPE_WAV = 2;
	public static final int MEDIATYPE_MP4 = 100;
	public static final int MEDIATYPE_FLV = 200;
	public static final int RT_TRUE = 0;
	public static final int RT_FALSE = 1;
	public static final double VERSION = 1.0;
	public static final int CHECK_CONNECT_OK = 0;
	public static final int CHECK_CONNECT_ERROR = 1;
	public static final int CHECK_DISCONNECT_OK = 0;
	public static final int CHECK_DISCONNECT_ERROR = 1;
	public static final int RT_DISCONNECT = 0;
	public static final int RT_CONNECTED = 1;
	public static final int IMGTYPE_JPG = 20;
	public static final int IMGTYPE_BMP = 21;
	public static final int PLAY_MEDIA = 1;
	public static final int SELECT_BOOK = 2;
	public static final int SELECT_PAGE = 3;
	public static final int PLAY_SEQUENCE = 4;
	public static final int PLAY_RANDOM = 5;

	private Handler mhandler;
	private Receiver receiver;
	private static int F1_Count = 0;
	private static int Connected = 0;
	private static int UserConnect = 0;
	private static int DeviceState = 1;
	private static int nPage = -1;
	private Resources res;
	private CodeAction codeAction;
	private ArrayList<MediaInfo> alMedia;
	private String packageName = "";
	private static AF6Receiver mInstance = null;
	private int id;
	private static MoniterTask taskBack = null;

	public AF6Receiver() {
		nPage = 0;
		codeAction = null;
		alMedia = null;
	}

	public static AF6Receiver getInstance() {
		if (mInstance == null) {
			synchronized (AF6Receiver.class) {
				if (mInstance == null) {
					mInstance = new AF6Receiver();
				}
			}
		}
		return mInstance;
	}

	private ArrayList<MediaInfo> AFI6GetCodeInfo(int iPage, int code[],
			CodeAction clickAction)

	{
		nPage = iPage;
		return receiver.AFI6GetCodeInfo(iPage, code, clickAction);
	}

	public int AFI6Initial() {
		kill_ps("afdevice");
		return receiver.AFI6Initial();
	}

	public int AFI6SetHandler(Handler handler) {
		mhandler = handler;
		return receiver.RT_TRUE;
	}

	public Handler AFI6GetHandler() {
		return mhandler;
	}

	public int AFI6SetResources(Resources rs) {
		res = rs;
		return receiver.RT_TRUE;
	}

	public int AFI6SetPackageName(String packName) {
		packageName = packName;
		return receiver.RT_TRUE;
	}

	public int AFI6Unitial() {
		MoniterTask.getInstance(hr, packageName).cancel(true);
		MoniterTask.getInstance(hr, packageName).stopC();
		MoniterTask.getInstance(hr, packageName).StopTask();
		return receiver.AFI6Unitial();
	}

	public int AFI6InitData(double version) {
		if (version <= LAST_VERSION) {
			receiver.AFI6InitData(version);
			return RT_TRUE;
		} else {
			return RT_FALSE;
		}
	}

	private int ConnectDevice() {
		if (Connected == 1)
			return receiver.CHECK_CONNECT_OK;
		if (Connected == 0 && DeviceState == 1) {
			try {
				String str1 = "/data/data/" + packageName + "/files";
				String str2 = "/data/data/" + packageName + "/files/afdevice";
				String str3 = "chmod 777 /data/data/" + packageName
						+ "/files/afdevice" + "\n";
				File file = new File(str1);
				file.mkdir();
				// if (!new File(str2)
				// .exists())
				CopyRAWtoSDCard(id, str2);
				Runtime runtime = Runtime.getRuntime();
				Process proc = runtime.exec(str3);
				// Log.e("run ", "" + str3);
				MoniterTask task = MoniterTask
						.getInstance(this.hr, packageName);
				if (taskBack != null) {
					if (task == taskBack) {
						task.StopTask();
						task.stopC();
						task = MoniterTask.getInstance(this.hr, packageName);
					}
				}
				taskBack = task;
				if (task != null)
					task.execute();
			} catch (Exception e) {

			}
			return receiver.CHECK_CONNECT_OK;
		} else {
			return receiver.CHECK_CONNECT_ERROR;
		}
	}

	public int AFI6ConnectDevice(int mid) {
		UserConnect = 1;
		id = mid;
		return ConnectDevice();
	}

	private int DisConnectDevice() {
		MoniterTask task = MoniterTask.getInstance(hr, packageName);
		if (task != null) {
			task.stopC();
		}
		Connected = 0;
		return CHECK_DISCONNECT_OK;
	}

	public int AFI6DisConnectDevice() {
		UserConnect = 0;
		return DisConnectDevice();
	}

	public int AFI6GetDeviceState() {
		if (Connected == 1) {
			return receiver.RT_CONNECTED;
		} else {
			return receiver.RT_DISCONNECT;
		}
	}

	private void CopyRAWtoSDCard(int id, String path) throws IOException {

		FileOutputStream out = new FileOutputStream(path);
		InputStream in = res.openRawResource(id);
		byte[] buff = new byte[1024];
		int read = 0;
		try {
			while ((read = in.read(buff)) > 0) {
				out.write(buff, 0, read);
			}
		} finally {
			in.close();
			out.close();
		}
	}

	public int AFI6StartReceiveIndex() {
		return receiver.AFI6StartReceiveIndex();
	}

	public int AFI6StopReceiveIndex() {
		return receiver.AFI6StopReceiveIndex();
	}

	public int AFI6SetBookName(String bookName) {
		if (bookName.toUpperCase().endsWith(".AP6")) {
			nPage = 0;
			return receiver.AFI6SetBookName(bookName);
		} else {
			return receiver.RT_FALSE;
		}
	}

	public int AFI6GetBookInfo(BookInfo bookInfo) {
		return receiver.AFI6GetBookInfo(bookInfo);
	}

	public int AFI6GetLessonInfo(int iLesson, LessonInfo lessonInfo) {
		return receiver.AFI6GetLessonInfo(iLesson, lessonInfo);
	}

	public int AFI6GetPageInfo(int iPage, PageInfo pageInfo) {
		nPage = iPage;
		return receiver.AFI6GetPageInfo(iPage, pageInfo);
	}

	public byte[] AFI6GetSeqItemInfo(int iSeqItem, MediaInfo mediaInfo) {
		return receiver.AFI6GetSeqItemInfo(iSeqItem, mediaInfo);
	}

	public ArrayList<MediaInfo> AFI6GetClickInfo(int iPage, int XPos, int YPos,
			CodeAction clickAction) {
		nPage = iPage;
		return receiver.AFI6GetClickInfo(iPage, XPos, YPos, clickAction);
	}

	public ArrayList<MediaInfo> AFI6GetCodeEvent(CodeAction cAction) {
		if (codeAction != null) {
			cAction.SetActionInfo(codeAction.actionInfo);
			cAction.SetActionType(codeAction.actionType);
			cAction.SetMediaCount(codeAction.mediaCount);
			cAction.SetMediaPlayCount(codeAction.mediaPlayCount);
			codeAction.actionInfo = -1;
			codeAction.actionType = -1;
			codeAction.mediaCount = -1;
			codeAction.mediaPlayCount = -1;
		}
		return alMedia;
	}

	private class MyHandler extends Handler {
		private final WeakReference<AF6Receiver> mActivity;

		public MyHandler(AF6Receiver recevier) {
			mActivity = new WeakReference<AF6Receiver>(recevier);
		}

		byte CharToByte(char c) {
			byte b = 0;
			if (c >= '0' && c <= '9') {

				b = (byte) (c - '0');
			} else if (c >= 'A' && c <= 'F') {
				b = (byte) (c - 'A' + (byte) 10);
			} else if (c >= 'a' && c <= 'f') {
				b = (byte) (c - 'a' + (byte) 10);
			}
			return b;
		}

		int[] ConverseToByte(String s) {
			if (s.length() != 28)
				return null;
			int bt[] = new int[14];
			for (int i = 0; i < 14; i++) {
				bt[i] = (byte) (CharToByte(s.charAt(i * 2)) * 0x10 + CharToByte(s
						.charAt(i * 2 + 1)));
			}
			return bt;
		}

		@Override
		public void handleMessage(Message msg) {
			AF6Receiver activity = mActivity.get();
			if (activity != null) {
				String s = (String) msg.obj;

				String str1 = "0";
				for (int i = 0; i < s.length(); i++) {
					str1 += Integer.toHexString(s.charAt(i));
				}

				if (msg.arg1 == 101 || msg.arg1 == 102) {
					if (mhandler != null) {
						Message msg1 = new Message();
						msg1.obj = "Exit";
						msg1.arg1 = 103;
						if (mhandler != null)
							mhandler.sendMessage(msg1);
					}
					MoniterTask task = MoniterTask.getInstance(hr, packageName);
					if (task != null)
						task.StopTask();
				}
				if (s.equals("Quit")) {
					Connected = 0;
					return;
				}
				if (s.equals("connected")) {
					Log.e("start ", "connected");
					if (mhandler != null) {
						Message msg1 = new Message();
						msg1.obj = "Connected";
						msg1.arg1 = 102;
						if (mhandler != null)
							mhandler.sendMessage(msg1);
					}
					Connected = 1;
					return;
				}
				// if (Connected == 1)
				{
					Log.e("recevie ", "" + s.length());
					if (s.length() >= 30) {
						int bt[] = ConverseToByte(s.substring(2));
						codeAction = new CodeAction();
						alMedia = receiver.AFI6GetCodeInfo(nPage, bt,
								codeAction);
						if (mhandler != null) {
							Message msg1 = new Message();
							msg1.obj = "Action";
							msg1.arg1 = 101;
							if (mhandler != null)
								mhandler.sendMessage(msg1);
						}
					}
				}
			}
		}
	}

	private final MyHandler hr = new MyHandler(this);

	private void kill_ps(String name) {
		String s = "/n";
		try {
			int first = 0;
			Process p = Runtime.getRuntime().exec("ps " + name);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				String[] names = line.replaceAll("\\s{1,}", " ").split(" ");

				if (first > 0) {
					Runtime runtime = Runtime.getRuntime();
					Process proc = runtime.exec("kill -9 " + names[1] + "\n");
				}
				first++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
