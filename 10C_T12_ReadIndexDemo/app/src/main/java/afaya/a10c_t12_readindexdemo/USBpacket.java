package afaya.a10c_t12_readindexdemo;

public class USBpacket
{
	public static class Constants
	{
		public final static int OID_TYPE_INDEX = 129;
		public final static int OID_TYPE_KEY = 2;
	}
	public USBpacket(int type, long index)
	{
		this.type = type;
		this.index = index;
	}
	public int type;
	public long index;
}
