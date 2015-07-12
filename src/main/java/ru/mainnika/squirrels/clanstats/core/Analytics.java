package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.$.Receiver;
import ru.mainnika.squirrels.clanstats.net.Client;
import ru.mainnika.squirrels.clanstats.net.Connection;
import ru.mainnika.squirrels.clanstats.net.Packet;
import ru.mainnika.squirrels.clanstats.net.Server;
import ru.mainnika.squirrels.clanstats.utils.GuardSolver;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Analytics implements Receiver
{
	private static final Logger log;
	private static final HashMap<Server, Method> handlers;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
		handlers = new HashMap<>();

		on(Server.HELLO, "onHello");
		on(Server.GUARD, "onGuard");
		on(Server.LOGIN, "onLogin");
	}

	private Connection io;
	private Credentials credentials;

	public Analytics(Connection connection, Credentials credentials)
	{
		this.io = connection;
		this.credentials = credentials;

		this.io.setReceiver(this);
	}

	public void onConnect()
	{
		this.sendPacket(Client.HELLO);
	}

	public void onDisconnect()
	{

	}

	public void onPacket(Packet packet)
	{
		int id = packet.getId();
		Server format = Server.getById(id);
		Method method = handlers.get(format);

		try
		{
			method.invoke(this, packet);
		} catch (Exception e)
		{
			log.warning("Something wrong with handler (" + e.getMessage() + ")");
		}
	}

	public void onHello(Packet packet)
	{
		log.info("Received hello");
	}

	public void onLogin(Packet packet)
	{
		byte status = packet.getByte(0);

		log.info("Received login with status " + status);
	}

	public void onGuard(Packet packet) throws IOException
	{
		log.info("Received guard");

		List<Byte> inflatedTask = packet.getArray(0);

		if (inflatedTask.size() > 0)
		{
			byte[] inflatedRaw = (byte[]) inflatedTask.toArray()[0];
			byte[] deflatedRaw = GuardSolver.deflate(inflatedRaw, 0, inflatedRaw.length);

			String deflatedTask = new String(deflatedRaw);
			String response = GuardSolver.solve(deflatedTask);

			log.info("Guard response " + response);

			this.sendPacket(Client.GUARD, response);
		}

		this.sendPacket(Client.LOGIN, this.credentials.uid(), this.credentials.type(), this.credentials.auth(), 0, 0);
	}

	public void sendPacket(Client format, Object... args)
	{
		try
		{
			this.io.send(format, args);
		} catch (IOException e)
		{
			log.warning("IO error " + e.getMessage());
		}
	}

	public static void on(Server format, String method)
	{
		try
		{
			handlers.put(format, Analytics.class.getMethod(method, Packet.class));
		} catch (NoSuchMethodException e)
		{
			log.warning("Can not register handler " + method + " (" + e.getMessage() + ")");
		}
	}
}
