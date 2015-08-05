package ru.mainnika.squirrels.clanstats.core;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.json.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChatBot
{
	Analytics owner;

	ChatBot(Analytics owner)
	{
		this.owner = owner;
	}

	public void request(String command) throws IOException
	{
		switch (command)
		{
			case "boobs":
				boobs();
				break;

			default:
				owner.clanChat("[error]: unknown command");
				break;
		}
	}

	private void boobs() throws IOException
	{
		HttpURLConnection connection;

		try
		{
			URL url = new URL("http://api.oboobs.ru/noise/1");

			connection = (HttpURLConnection) url.openConnection();

		} catch (MalformedURLException e)
		{
			throw new IOException("Invalid boobs url " + e.getMessage());
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

		Context cx = Context.enter();
		JsonParser parser = new JsonParser(cx, cx.initStandardObjects());
		Object object;

		try
		{
			object = parser.parseValue(body.toString());
		} catch (JsonParser.ParseException ignored)
		{
			return;
		}

		String picture = (String) ((NativeObject) ((NativeArray) object).get(0)).get("preview");
		this.owner.clanChat(String.format("<img&#0;src='' height='100' width='100' src='%s%s' >", "http://media.oboobs.ru/", picture));
	}


}
