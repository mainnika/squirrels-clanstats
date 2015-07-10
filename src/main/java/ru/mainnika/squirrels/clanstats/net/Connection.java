package ru.mainnika.squirrels.clanstats.net;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class Connection implements Runnable
{
	private static final Logger log;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
	}

	private static final int bufferLen = 1024 * 10;
	private static final int sizeLen = 4;

	private Thread thread;
	private Socket socket;
	private Semaphore semaphore;
	private String host;
	private int port;

	private InputStream input;
	private OutputStream output;

	private Connection(String host, int port)
	{
		this.host = host;
		this.port = port;

		this.semaphore = new Semaphore(0, true);
	}

	private void connect() throws IOException
	{
		InetSocketAddress addr = new InetSocketAddress(this.host, this.port);

		this.socket = new Socket();
		this.socket.connect(addr);
	}

	private void parser(Byte[] data)
	{
		log.info("Received data with size " + data.length);
	}

	private void receiver() throws IOException
	{
		ArrayList<Byte> dataBuffer = new ArrayList<>(bufferLen);
		byte[] rawBuffer = new byte[bufferLen];
		int expectation = 0;

		while (true)
		{
			int read = this.input.read(rawBuffer, 0, bufferLen);

			if (read < 0)
			{
				break;
			}

			byte[] data = ArrayUtils.subarray(rawBuffer, 0, read);
			dataBuffer.addAll(Arrays.asList(ArrayUtils.toObject(data)));

			while (expectation <= dataBuffer.size())
			{
				if (expectation == 0 && dataBuffer.size() < sizeLen)
				{
					break;
				}

				if (expectation == 0)
				{
					Byte[] objectArray = dataBuffer.toArray(new Byte[dataBuffer.size()]);
					ByteBuffer wrapped = ByteBuffer.wrap(ArrayUtils.toPrimitive(objectArray));

					wrapped.order(ByteOrder.LITTLE_ENDIAN);

					expectation = wrapped.getInt();
					dataBuffer = new ArrayList<>(dataBuffer.subList(4, dataBuffer.size()));

					continue;
				}

				List<Byte> sublist = dataBuffer.subList(0, expectation);
				Byte[] objectSublist = sublist.toArray(new Byte[sublist.size()]);

				this.parser(objectSublist);

				dataBuffer = new ArrayList<>(dataBuffer.subList(expectation, dataBuffer.size()));
				expectation = 0;
			}
		}

		this.input = null;
		this.output = null;

		throw new IOException("Disconnected");
	}

	public void send(byte[] buffer) throws IOException
	{
		this.output.write(buffer);
	}

	public void run()
	{
		while (true)
		{
			try
			{
				log.info("Trying to connect...");

				this.connect();

				this.input = this.socket.getInputStream();
				this.output = this.socket.getOutputStream();

				log.info("Connected!");

				this.semaphore.release();

				this.receiver();

				break;

			} catch (IOException e)
			{
				log.warning("Can't connect to server, " + e.getMessage());
			}

			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException ignored)
			{
			}
		}
	}

	public void start()
	{
		this.thread.start();
		this.semaphore.acquireUninterruptibly();
	}

	public static Connection create(String host, int port)
	{
		log.info("Creating instance with " + host + ":" + port);

		Connection instance = new Connection(host, port);
		instance.thread = new Thread(instance);

		return instance;
	}
}
