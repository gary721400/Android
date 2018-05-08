package afaya.player;

import java.io.Serializable;
import java.util.ArrayList;

import android.R.integer;

public class CodeAction implements Serializable {
	public int actionType;
	public int actionInfo;
	public int mediaCount;
	public int mediaPlayCount;
	public int imagePlayTime;
	public int reserved01;

	public CodeAction() {
		actionType = 0;
		actionInfo = 0;
		mediaCount = 0;
		mediaPlayCount = 0;
		imagePlayTime = 0;
		reserved01 = 0;
	}

	public void SetActionType(int action_type) {
		actionType = action_type;
	}

	public void SetActionInfo(int action_Info) {
		actionInfo = action_Info;
	}

	public void SetMediaCount(int media_Count) {
		mediaCount = media_Count;
	}

	public void SetMediaPlayCount(int media_PlayCount) {
		mediaPlayCount = media_PlayCount;
	}

	public void SetImagePlayTime(int image_playtime) {
		imagePlayTime = image_playtime;
	}

	public void SetReserved01(int reserved_01) {
		reserved01 = reserved_01;
	}

}
