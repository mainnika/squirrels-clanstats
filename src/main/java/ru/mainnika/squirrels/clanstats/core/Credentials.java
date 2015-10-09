package ru.mainnika.squirrels.clanstats.core;

public class Credentials
{
	private long uid;
	private byte type;
	private byte oauth;
	private String auth;

	public Credentials(long uid, int type, String auth)
	{
		this.uid = uid;
		this.type = (byte) type;
		this.oauth = 0;
		this.auth = auth;
	}

	public long uid()
	{
		return this.uid;
	}

	public byte type()
	{
		return this.type;
	}

	public byte oauth()
	{
		return this.oauth;
	}

	public String auth()
	{
		return this.auth;
	}


}
