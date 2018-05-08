package afaya.player;

import java.io.Serializable;

public class MediaInfo implements Serializable {	

	public long mediaType;
	public long divID;
	public long startAddress;
	public long size;
	public long startTime;
	public long duration;
	public long reserved01;
	public long reserved02;

	public MediaInfo() {
		startAddress = 0;
		size = 0;
		mediaType = 0;
		divID = 0;
		startTime = 0;
		duration = 0;
		reserved01 = 0;
		reserved02 = 0;
	}

	public void SetStartAddress(long stAdd) {
		startAddress = stAdd;
	}

	public void SetSize(long sz) {
		size = sz;
	}

	public void SetMediaType(long mType) {
		mediaType = mType;
	}

	public void SetDivID(long div_ID) {
		divID = div_ID;
	}

	public void SetStartTime(long start_time) {
		startTime = start_time;
	}

	public void SetDuration(long durationValue) {
		duration = durationValue;
	}

	public void SetReserved01(long reserved_01) {
		reserved01 = reserved_01;
	}

	public void SetReserved02(long reserved_02) {
		reserved02 = reserved_02;
	}

};