package afaya.player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Receiver implements Serializable{

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

	static {

		System.loadLibrary("AFI6");
	}

	public native static int AFI6Initial();

	public native static int AFI6Unitial();

	public native static int AFI6InitData(double version);

	public native static int AFI6ConnectDevice();

	public native static int AFI6DisConnectDevice();

	public native static int AFI6StartReceiveIndex();

	public native static int AFI6StopReceiveIndex();

	public native static int AFI6GetDeviceState();

	public native static int AFI6SetBookName(String bookName);

	public native static int AFI6GetBookInfo(BookInfo bookInfo);

	public native static int AFI6GetLessonInfo(int iLesson,
			LessonInfo lessonInfo);

	public native static int AFI6GetPageInfo(int iPage, PageInfo pageInfo);

	public native static byte[] AFI6GetSeqItemInfo(int iSeqItem,
			MediaInfo mediaInfo);

	public native static ArrayList<MediaInfo> AFI6GetClickInfo(int iPage,
			int XPos, int YPos, CodeAction clickAction);

	public native static ArrayList<MediaInfo> AFI6GetCodeInfo(int iPage,
			int code[], CodeAction clickAction);
	
	public Receiver()
	{
		return;
	}
}
