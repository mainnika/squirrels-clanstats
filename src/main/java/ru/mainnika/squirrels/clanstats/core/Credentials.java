package ru.mainnika.squirrels.clanstats.core;

public class Credentials
{
	private long uid;
	private byte type;
	private boolean oauth;
	private String auth;

	public Credentials(long uid, int type, String auth)
	{
		this.uid = uid;
		this.type = (byte) type;
		this.oauth = false;
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

	public boolean oauth()
	{
		return this.oauth;
	}

	public String auth()
	{
		return this.auth;
	}


}
