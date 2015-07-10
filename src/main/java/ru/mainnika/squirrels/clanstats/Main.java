package ru.mainnika.squirrels.clanstats;

import ru.mainnika.squirrels.clanstats.net.Connection;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Main
{

	public static void main(String[] args) throws Exception
	{

		Connection net = Connection.create("88.212.207.7", 22227);

		net.start();

		byte[] hello = {0x06, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x08,0x00};
		net.send(hello);

		Thread.sleep(20000);

	}



}
