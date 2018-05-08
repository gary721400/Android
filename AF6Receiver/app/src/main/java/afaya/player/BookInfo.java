package afaya.player;

import java.io.Serializable;

public class BookInfo implements Serializable{   
	public int lessonCount;
	public MediaInfo lessonBegin;
	public MediaInfo lessonEnd;
	
	public BookInfo()
	{
		lessonCount = 0;
		lessonBegin = null;
		lessonEnd = null;
	}
	
}
