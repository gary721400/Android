package afaya.player;

import java.io.Serializable;

public class LessonInfo  implements Serializable{
	public int pagesCount;
	public int[] pageIDList;
	public LessonInfo()
	{
		pagesCount = 0;
		pageIDList = null;
	}	
}
