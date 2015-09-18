package ru.mainnika.squirrels.clanstats.core;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.json.JsonParser;
import ru.mainnika.squirrels.clanstats.Main;
import ru.mainnika.squirrels.clanstats.analytics.AnalyticSnapshot;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatBot
{
	public enum ChatType
	{
		ROOM,
		CLAN,
		COMMON,
		NEWBIE
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

			case "debug":
				debug();
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

	private void debug() throws IOException
	{
		String[] debugs = Main.getVersion().split("\n");

		this.owner.clanChat("[debug]:");

		for (String line : debugs)
		{
			this.owner.clanChat(line);
		}
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

		Date date = DateTime.fromUnixhour(hour);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

		this.owner.clanChat("[stats]: " + sdf.format(date));

		last.sort(new Comparator<AnalyticSnapshot.Snapshot>()
		{
			@Override
			public int compare(AnalyticSnapshot.Snapshot o1, AnalyticSnapshot.Snapshot o2)
			{
				return o2.value - o1.value;
			}
		});

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

			this.owner.clanChat(playerName + " ---- > " + playerValue);
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
