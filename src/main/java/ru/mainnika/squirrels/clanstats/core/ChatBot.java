package ru.mainnika.squirrels.clanstats.core;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.json.JsonParser;
import ru.mainnika.squirrels.clanstats.analytics.AnalyticSnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ChatBot
{
	public enum ChatType
	{
		ROOM,
		CLAN,
		COMMON,
		NEWBIE;
	}

	Analytics owner;

	ChatBot(Analytics owner)
	{
		this.owner = owner;
	}

	public void request(String command) throws IOException
	{
		switch (command)
		{
			case "hello":
				hello();
				break;

			case "boobs":
				boobs();
				break;

			case "stats":
				stats();
				break;

			default:
				error();
				break;
		}
	}

	private void hello() throws IOException
	{
		this.owner.clanChat("[hello]: hello!");
	}

	private void stats() throws IOException
	{
		List<AnalyticSnapshot.Snapshot> last = ru.mainnika.squirrels.clanstats.analytics.Analytics.CLAN_MEMBERS_HOURLY.instance().getLast(this.owner.clanId());

		if (last == null || last.isEmpty())
		{
			this.owner.clanChat("[stats]: stats is empty");
			return;
		}

		Integer hour = last.get(0).hash;
		this.owner.clanChat("[stats]: last hour = " + hour);

		for (AnalyticSnapshot.Snapshot snapshot : last)
		{
			Integer playerId = snapshot.data;
			Integer playerValue = snapshot.value;
			String playerName = playerId.toString();

			Player player = PlayersCache.getInstance().get(playerId);

			if (player != null)
			{
				playerName = player.name();
			}

			this.owner.clanChat(playerName + " -> " + playerValue);
		}
	}

	private void error() throws IOException
	{
		owner.clanChat("[error]: unknown command");
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
		this.owner.clanChat(String.format("<img&#0;src='' height='100' width='100' src='%s%s' >", "//proxypass.azurewebsites.net/?http://media.oboobs.ru/", picture));
	}


}
