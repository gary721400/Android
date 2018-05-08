package afaya.player;

import java.io.Serializable;

public class PageInfo  implements Serializable{
	public int seqItemCount;
	public ImageInfo pageBackImg;
	public MediaInfo action;
	public PageInfo() {
		seqItemCount = 0;
		pageBackImg = null;
		action = null;
	}
}
