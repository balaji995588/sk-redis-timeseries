package com.redis_timeseries.feed;

public interface IBaseStruct {
	public void ByteToStruct(byte[] data);

	public byte[] StructToByte();
}
