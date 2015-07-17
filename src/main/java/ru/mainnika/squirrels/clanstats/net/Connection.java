package ru.mainnika.squirrels.clanstats.net;

import org.apache.commons.lang3.ArrayUtils;
import ru.mainnika.squirrels.clanstats.net.packets.Client;
import ru.mainnika.squirrels.clanstats.net.packets.Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
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
	private Receiver receiver;
	private int port;
	private int id;

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

		this.id = 0;
		this.socket = new Socket();
		this.socket.connect(addr);
	}

	private void parser(byte[] data)
	{
		ByteBuffer wrapped_data = ByteBuffer.wrap(data);

		wrapped_data.order(ByteOrder.LITTLE_ENDIAN);

		short type = wrapped_data.getShort();

		Server format = Server.getById(type);

		if (format == null)
		{
			log.warning("Received unknown packet with type " + type);
			return;
		}

		byte[] packetRaw = Arrays.copyOfRange(data, 2, data.length);

		log.info("Received packet " + format);

		Packet packet = Packet.make(format.mask(), format.id(), packetRaw);

		if (this.receiver != null)
		{
			this.receiver.onPacket(packet);
		}
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
				this.parser(out.toByteArray());
				out.reset();
				expectation = 0;
				received = 0;
			}
		}

		this.input = null;
		this.output = null;

		throw new IOException("Disconnected");
	}

	public void send(Client format, Object... args) throws IOException
	{
		Packet packet = Packet.make(format.mask(), format.id(), args);
		Byte[] rawPacket = packet.getRaw();

		ByteBuffer bufferPacket = ByteBuffer.allocate(4 + 4 + 2 + rawPacket.length);

		bufferPacket.order(ByteOrder.LITTLE_ENDIAN);

		bufferPacket.putInt(rawPacket.length + 4 + 2);
		bufferPacket.putInt(this.id++);
		bufferPacket.putShort((short) (int) format.id());
		bufferPacket.put(ArrayUtils.toPrimitive(rawPacket));

		this.output.write(bufferPacket.array());
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
					this.receiver.onConnect();
				}

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

	public void setReceiver(Receiver receiver)
	{
		if (this.receiver != null)
		{
			this.receiver.onDisconnect();
			this.receiver = null;
		}

		this.receiver = receiver;
	}

	public static Connection create(String host, int port)
	{
		log.info("Creating instance with " + host + ":" + port);

		Connection instance = new Connection(host, port);
		instance.thread = new Thread(instance);

		return instance;
	}
}
