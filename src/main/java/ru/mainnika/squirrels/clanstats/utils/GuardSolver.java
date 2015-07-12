package ru.mainnika.squirrels.clanstats.utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import ru.mainnika.squirrels.clanstats.net.Connection;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.InflaterInputStream;

public class GuardSolver
{
	private static final String configUrl = "http://squirrelsb.realcdn.ru/release/config.js";
	private static final String swfUrl = "http://squirrelsb.realcdn.ru/release/client_release";
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
			throw new IOException("Invalid config url " + e.getMessage());
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

	public static byte[] deflate(byte[] inflated, int offset, int len) throws IOException
	{
		ByteArrayInputStream byteIn = new ByteArrayInputStream(inflated, offset, len);
		InflaterInputStream zip = new InflaterInputStream(byteIn);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

		int size;
		byte[] buffer = new byte[len];
		while ((size = zip.read(buffer)) != -1)
		{
			if (size == 0)
				continue;

			byteOut.write(buffer, 0, size);
		}

		return byteOut.toByteArray();
	}

	public static String md5sum(byte[] buffer) throws IOException
	{
		MessageDigest md;

		try
		{
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e)
		{
			throw new IOException(e);
		}

		byte[] digest = md.digest(buffer);

		StringBuilder sb = new StringBuilder();
		for (byte aDigest : digest)
		{
			sb.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	public static String getClientHash(int version) throws IOException
	{
		if (version == cachedVersion)
		{
			return cachedHash;
		}

		HttpURLConnection connection;

		try
		{
			URL url = new URL(swfUrl + Integer.toString(version) + ".swf");

			connection = (HttpURLConnection) url.openConnection();

		} catch (MalformedURLException e)
		{
			throw new IOException("Invalid swf url " + e.getMessage());
		}

		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			throw new IOException("Can not get swf, status code " + connection.getResponseCode());
		}

		int size;
		int swfSize = connection.getContentLength();
		byte[] rawBuffer = new byte[1024 * 1024];
		byte[] swfBody = new byte[swfSize];
		ByteBuffer swfBuffer = ByteBuffer.wrap(swfBody);
		InputStream in = connection.getInputStream();

		while ((size = in.read(rawBuffer)) != -1)
		{
			if (size == 0)
				continue;

			swfBuffer.put(rawBuffer, 0, size);
			log.info("Reading swf chunk size " + size);
		}

		log.info("Buffer size " + swfBuffer.position());

		if (swfBody[0] == 'C')
		{
			byte[] deflated = deflate(swfBody, 8, swfBuffer.position());
			byte[] origSwf = new byte[deflated.length + 8];

			origSwf[0] = 'F';

			System.arraycopy(swfBody, 1, origSwf, 1, 7);
			System.arraycopy(deflated, 0, origSwf, 8, deflated.length);

			swfBody = origSwf;
		}

		cachedHash = md5sum(swfBody);
		cachedVersion = version;

		return cachedHash;
	}

	public static String solve(String task)
	{
		try
		{
			int currentVersion = getClientVersion();
			String cachedHash = getClientHash(currentVersion);

			String init = "var by_blooddy_crypto_MD5={hashBytes:function(){return\"%s\";}},root={root:null,loaderInfo:{bytes:null}};root.root=root;";
			String context = String.format(init, cachedHash);

			task = task.replaceAll("\\(([a-z]+)\\sin\\s([0-9]+)\\.\\.\\.([0-9]+)\\)", "(var $1 = $2; $1 < $3; $1 ++)");
			task = task.replaceAll("return", "");

			Context ctx = Context.enter();
			Scriptable scope = ctx.initStandardObjects();
			Object obj = ctx.evaluateString(scope, context + task, null, 0, null);
			Context.exit();

			return md5sum(((String) obj).getBytes());

		} catch (IOException e)
		{
			log.warning("Unable to solve guard " + e.getMessage());
			return null;
		}
	}

}
