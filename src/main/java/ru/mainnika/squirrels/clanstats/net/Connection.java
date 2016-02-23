package ru.mainnika.squirrels.clanstats.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	private String host;
	private Receiver receiver;
	private int port;

	private InputStream input;
	private OutputStream output;

	private Connection(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	public void connect() throws IOException
	{
		InetSocketAddress addr = new InetSocketAddress(this.host, this.port);

		this.socket = new Socket();
		this.socket.connect(addr);
	}

	public void disconnect()
	{
		try
		{
			this.socket.close();

		} catch (IOException err)
		{
			log.warning("Can't disconnect, error: " + err.getMessage());
		}
	}

	private void incoming(byte[] data)
	{
		if (this.receiver == null)
		{
			return;
		}

		this.receiver.onPacket(data);
	}

	private void receiver() throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		byte[] rawBuffer = new byte[bufferLen];
		byte[] expectationBuffer = new byte[4];

		int expectation = 0;
		int received = 0;

		while (true)
		{
			int read;

			if (expectation == 0)
			{
				read = this.input.read(expectationBuffer, 0, sizeLen - received);
				received += read;

				if (read < 0)
				{
					break;
				}

				if (received == sizeLen)
				{
					ByteBuffer expectationWrapped = ByteBuffer.wrap(expectationBuffer);
					expectationWrapped.order(ByteOrder.LITTLE_ENDIAN);
					expectation = expectationWrapped.getInt();
					received = 0;
				}

				continue;
			}

			int needle = Math.min(expectation - received, bufferLen);

			read = this.input.read(rawBuffer, 0, needle);
			received += read;

			if (read < 0)
			{
				break;
			}

			out.write(rawBuffer, 0, read);

			if (received == expectation)
			{
				this.incoming(out.toByteArray());
				out.reset();
				expectation = 0;
				received = 0;
			}
		}

		this.input = null;
		this.output = null;

		throw new IOException("Disconnected");
	}

	public void send(byte[] data) throws IOException
	{
		this.output.write(data);
		this.output.flush();
	}

	public void send(ByteBuffer buffer) throws IOException
	{
		this.output.write(buffer.array());
		this.output.flush();
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

				if (this.receiver != null)
				{
					this.receiver.onConnect_impl();
				}

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
				break;
			}
		}
	}

	public void start()
	{
		this.thread.start();
	}

	public void stop()
	{
		this.thread.interrupt();
//		this.thread.join();
	}


	public void setReceiver(Receiver receiver)
	{
		if (this.receiver != null)
		{
			this.receiver.onDisconnect_impl();
		}

		this.receiver = receiver;
	}

	public static Connection create(String host, int port)
	{
		log.info("Creating instance with " + host + ":" + port);

		Connection instance = new Connection(host, port);
		instance.thread = new Thread(instance);
		instance.thread.setDaemon(true);

		return instance;
	}
}
