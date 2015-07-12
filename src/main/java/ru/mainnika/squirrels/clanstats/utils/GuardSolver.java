package ru.mainnika.squirrels.clanstats.utils;

import ru.mainnika.squirrels.clanstats.net.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuardSolver
{
	private static final String configUrl = "http://squirrelsb.realcdn.ru/release/config.js";
	private static final Pattern versionPattern = Pattern.compile(".*version\\s=\\s(?<version>[0-9]+).*");
	private static final Logger log;

	private static int cachedVersion;
	private static String cachedHash;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
	}

	private GuardSolver()
	{
	}

	public static int getClientVersion() throws IOException
	{
		HttpURLConnection connection;

		try
		{
			URL url = new URL(configUrl + "?" + Math.random());

			connection = (HttpURLConnection) url.openConnection();

		} catch (MalformedURLException e)
		{
			throw new IOException("Invalid config url" + configUrl);
		}

		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			throw new IOException("Can not get config, status code " + connection.getResponseCode());
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder body = new StringBuilder();
		String part;

		while ((part = in.readLine()) != null)
		{
			body.append(part);
		}

		Matcher m = versionPattern.matcher(body.toString());

		boolean has = m.matches();

		if (!has)
		{
			throw new IOException("Can not find version in config");
		}

		return Integer.parseInt(m.group("version"));
	}

	public String solve(String task)
	{
		return null;
	}

}
