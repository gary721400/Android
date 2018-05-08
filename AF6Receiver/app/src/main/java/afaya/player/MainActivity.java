package afaya.player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	AF6Receiver af6Receiver;
	private final MyHandler hr = new MyHandler(this);
	TextView LogTextViewMsg;
	TextView LogTextViewIndex;
	TextView LogTextViewCount;
	long indexCount = 0;
	long actionCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.e("", "");
		af6Receiver = new AF6Receiver();
		Resources rs = getResources();
		af6Receiver.AFI6Initial();
		Log.e("onCreate","init data = "+af6Receiver.AFI6InitData(3.0));
		af6Receiver.AFI6SetHandler(hr);
		af6Receiver.AFI6SetPackageName("afaya.player");
		af6Receiver.AFI6SetResources(rs);
		af6Receiver.AFI6ConnectDevice(R.raw.afdevice);
		LogTextViewMsg = (TextView) findViewById(R.id.testMsg);
		LogTextViewIndex = (TextView) findViewById(R.id.testMsgIndex);
		LogTextViewCount = (TextView) findViewById(R.id.testMsgCount);
		String filename = Environment.getExternalStorageDirectory().getPath();
		filename += "/Ap6/testap6.ap6";
		Log.e("filename", "" + filename);
		Log.e("Open file = ", "" + af6Receiver.AFI6SetBookName(filename));
		PlayClassInfo();
		PageInfo pageinfo = new PageInfo();
		af6Receiver.AFI6GetPageInfo(0, pageinfo);
		PlayClick(0, 100, 100);
		PlayClick(0, 200, 200);
		PlayClick(0, 300, 300);
		PlayClick(0, 400, 400);
		PlayClick(0, 500, 500);		
	}

	private void PlayClassInfo() {
		BookInfo bookInfo = new BookInfo();
		Log.e("ReadBookInfo = ", "" + af6Receiver.AFI6GetBookInfo(bookInfo));
		Log.e("LessonCount = ", "" + bookInfo.lessonCount);
		Log.e("LessonBegin = ", "" + bookInfo.lessonBegin.startAddress);
		Log.e("LessonEnd = ", "" + bookInfo.lessonEnd.startAddress);

		for (int less = 0; less < bookInfo.lessonCount; less++) {
			LessonInfo lessonInfo = new LessonInfo();
			af6Receiver.AFI6GetLessonInfo(less, lessonInfo);
			Log.e("less " + less, "PageCount " + lessonInfo.pagesCount);
			int[] pi = lessonInfo.pageIDList;
			for (int ip = 0; ip < lessonInfo.pagesCount; ip++) {
				Log.e("PageID " + ip, "=" + (pi[ip] - 1));
				PageInfo paInfo = new PageInfo();
				Log.e("PageMax " + pi[ip],
						"Max = "
								+ af6Receiver.AFI6GetPageInfo((pi[ip] - 1),
										paInfo));
				if (paInfo != null) {
					Log.e("PageID " + pi[ip], "seq count = "
							+ paInfo.seqItemCount);
					if (paInfo.pageBackImg != null) {
						Log.e("Image start ",
								"0x"
										+ Long.toHexString(paInfo.pageBackImg.startAddress));
						Log.e("Image size ",
								"0x"
										+ Long.toHexString(paInfo.pageBackImg.size));
						Log.e("Image type ",
								"0x"
										+ Long.toHexString(paInfo.pageBackImg.imgType));
						Log.e("Image div ",
								"0x"
										+ Long.toHexString(paInfo.pageBackImg.divID));
						Log.e("Image width ",
								"0x"
										+ Long.toHexString(paInfo.pageBackImg.imgWidth));
						Log.e("Image height ",
								"0x"
										+ Long.toHexString(paInfo.pageBackImg.imgHeight));
					}
					for (int j = 0; j < paInfo.seqItemCount; j++) {
						MediaInfo seqMedia = new MediaInfo();
						byte[] cap = af6Receiver
								.AFI6GetSeqItemInfo(j, seqMedia);
						
						if (cap != null) {
							Log.e("Cap len", "" + cap.length);
							String lhex = "";

							for (int k = 0; k < cap.length; k++) {
								String he = Integer.toHexString(cap[k]);
								if (he.length() == 1) {
									lhex += "0";
									lhex += he;
								} else if (he.length() == 2) {
									lhex += he;
								} else {
									lhex += he.substring(he.length() - 2,
											he.length());
								}
								lhex += " ";
							}
							   


							Log.e("Cap 0x", "" + lhex);
							Log.e("2seq adr = " + j,
									""
											+ Long.toHexString(seqMedia.startAddress));
							Log.e("3seq siz = " + j,
									"" + Long.toHexString(seqMedia.size));
							Log.e("4seq typ = " + j,
									"" + Long.toHexString(seqMedia.mediaType));
							Log.e("5seq DIV = " + j,
									"" + Long.toHexString(seqMedia.divID));
							Log.e("5seq startTime = " + j,
									"" + Long.toHexString(seqMedia.startTime));
							Log.e("5seq endTime = " + j,
									"" + Long.toHexString(seqMedia.duration));
							Log.e("5seq external01 = " + j,
									"" + Long.toHexString(seqMedia.reserved01));
							Log.e("5seq external02 = " + j,
									"" + Long.toHexString(seqMedia.reserved02));

						}
					}
				}
			}
		}
	}

	private void PlayClick(int iPage, int XPos, int YPos) {
		Log.e("x y =", "" + XPos + " " + YPos);
		CodeAction clickAction = new CodeAction();
		ArrayList<MediaInfo> imageInfo = af6Receiver.AFI6GetClickInfo(iPage,
				XPos, YPos, clickAction);

		if (clickAction != null) {
			Log.e("info = ", "" + Long.toHexString(clickAction.actionInfo));
			Log.e("type = ", "" + Long.toHexString(clickAction.actionType));
			Log.e("cont = ", "" + Long.toHexString(clickAction.mediaCount));
			Log.e("plct = ", "" + Long.toHexString(clickAction.mediaPlayCount));
			Log.e("time = ", "" + Long.toHexString(clickAction.imagePlayTime));
			Log.e("extr = ", "" + Long.toHexString(clickAction.reserved01));
			if (imageInfo != null) {
				for (int i = 0; i < imageInfo.size(); i++) {
					Log.e("addr = ",
							""
									+ Long.toHexString(imageInfo.get(i).startAddress));
					Log.e("size = ",
							"" + Long.toHexString(imageInfo.get(i).size));
					Log.e("type = ",
							"" + Long.toHexString(imageInfo.get(i).mediaType));
					Log.e("divd = ",
							"" + Long.toHexString(imageInfo.get(i).divID));
					Log.e("sTim = ",
							"" + Long.toHexString(imageInfo.get(i).startTime));
					Log.e("eTim = ",
							"" + Long.toHexString(imageInfo.get(i).duration));
					Log.e("ext1 = ",
							"" + Long.toHexString(imageInfo.get(i).reserved01));
					Log.e("ext2 = ",
							"" + Long.toHexString(imageInfo.get(i).reserved02));
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {

		af6Receiver.AFI6Unitial();
		super.onDestroy();
	}

	private class MyHandler extends Handler {
		private final WeakReference<MainActivity> mActivity;

		public MyHandler(MainActivity recevier) {
			mActivity = new WeakReference<MainActivity>(recevier);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity activity = mActivity.get();
			if (activity != null) {
				String s = (String) msg.obj;
				Log.e("msg = ", s);
				if (s.startsWith("Action")) {
					int arg1 = (Integer) msg.arg1;
					Log.e("arg1 = ", "" + arg1);
					CodeAction codeAction = new CodeAction();
					ArrayList<MediaInfo> alMedia = af6Receiver
							.AFI6GetCodeEvent(codeAction);
					Log.e("cod actionType = ", "" + codeAction.actionType);
					Log.e("cod actionInfo = ", "" + codeAction.actionInfo);
					Log.e("cod mediaCount = ", "" + codeAction.mediaCount);
					Log.e("cod mediaPlayCount = ", ""
							+ codeAction.mediaPlayCount);
					Log.e("ret = ", "" + alMedia);
					indexCount++;
					if (alMedia != null) {
						actionCount++;
						for (int i = 0; i < alMedia.size(); i++) {
							Log.e("addr = ", "" + alMedia.get(i).startAddress);
							Log.e("size = ", "" + alMedia.get(i).size);
							Log.e("type = ",
									""
											+ Long.toString(alMedia.get(i).mediaType));
						}
					}
					LogTextViewIndex.setText(Long.toString(indexCount));
					LogTextViewCount.setText(Long.toString(actionCount));
				} else if (s.startsWith("Connected")) {
					Log.e("start receiver ", "code");
					LogTextViewMsg.setText("Connected");
				}
			}
		}
	}
}
