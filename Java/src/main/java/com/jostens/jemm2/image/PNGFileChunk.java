package com.jostens.jemm2.image;

import java.nio.ByteBuffer;

public class PNGFileChunk
{
	private byte[] length;
	private byte[] type;
	private byte[] data;
	private byte[] crc;
	
	public byte[] getLength()
	{
		return length;
	}
	public void setLength(byte[] length)
	{
		this.length = length;
	}
	public byte[] getType()
	{
		return type;
	}
	public void setType(byte[] type)
	{
		this.type = type;
	}
	public byte[] getData()
	{
		return data;
	}
	public void setData(byte[] data)
	{
		this.data = data;
	}
	public byte[] getCrc()
	{
		return crc;
	}
	public void setCrc(byte[] crc)
	{
		this.crc = crc;
	}

	public int getIntLength()
	{
		return ByteBuffer.wrap(length, 0, 4).getInt();
	}
	
}
