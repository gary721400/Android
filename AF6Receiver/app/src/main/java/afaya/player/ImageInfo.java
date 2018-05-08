package afaya.player;

import java.io.Serializable;

public class ImageInfo implements Serializable {
	public long imgType;
	public long startAddress;
	public long size;
	public long imgWidth;
	public long imgHeight;
	public long divID;
	public long reserved01;
	public long reserved02;
	public ImageInfo() {
		imgType = 0;
		imgWidth = 0;
		imgHeight = 0;
		startAddress = 0;
		size = 0;
		divID = 0;
		reserved01 = 0;
		reserved02 = 0;
	}

	public void SetImageType(long image_Type) {
		imgType = image_Type;
	}

	public void SetImageWidth(long img_Width) {
		imgWidth = img_Width;
	}

	public void SetImageHeight(long img_Height) {
		imgHeight = img_Height;
	}

	public void SetStartAddress(long start_Addr) {
		startAddress = start_Addr;
	}

	public void SetSize(long image_Size) {
		size = image_Size;
	}

	public void SetDivID(long div_ID) {
		divID = div_ID;
	}

	public void SetReserved01(long reserved_01) {
		reserved01 = reserved_01;
	}

	public void SetReserved02(long reserved_02) {
		reserved02 = reserved_02;
	}
}
