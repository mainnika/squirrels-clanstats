package ru.mainnika.squirrels.clanstats.storage;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database
{
	private static final Database instance;

	static
	{
		instance = new Database();
	}

	private Database()
	{
	}
}
