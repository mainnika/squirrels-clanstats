package ru.mainnika.squirrels.clanstats;

import ru.mainnika.squirrels.clanstats.core.Analytics;
import ru.mainnika.squirrels.clanstats.core.Credentials;
import ru.mainnika.squirrels.clanstats.net.Connection;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		Connection net = Connection.create("88.212.207.7", 22236);
		Credentials cred = new Credentials(8479389, 0, "292e587617848804a15d6347ed80b1f6");
		Analytics analytics = new Analytics(net, cred);

		net.start();
	}
}
