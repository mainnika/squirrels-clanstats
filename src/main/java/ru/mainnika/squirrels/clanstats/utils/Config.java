package ru.mainnika.squirrels.clanstats.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Config
{
	private static String dbBase;
	private static String dbUser;
	private static String dbPassword;
	private static String dbHost;
	private static String dbPort;

	static
	{
		String dbBase = null;
		String dbUser = null;
		String dbPassword = null;
		String dbHost = null;
		String dbPort = null;

		try
		{
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(loader.getResource("config.xml").getFile()));

			Element config = doc.getDocumentElement();

			Element storage = (Element) config.getElementsByTagName("Storage").item(0);
			dbBase = getTextValue(storage, "Database");
			dbUser = getTextValue(storage, "User");
			dbPassword = getTextValue(storage, "Password");
			dbHost = getTextValue(storage, "Host");
			dbPort = getTextValue(storage, "Port");

		} catch (Exception e)
		{
			dbBase = "sqclanbase";
			dbUser = "root";
			dbPassword = "";
			dbHost = "localhost";
			dbPort = "3306";
		} finally
		{
			Config.dbBase = dbBase;
			Config.dbUser = dbUser;
			Config.dbPassword = dbPassword;
			Config.dbHost = dbHost;
			Config.dbPort = dbPort;
		}
	}

	private static String getTextValue(Element ele, String tagName)
	{
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0)
		{
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	public static String dbUser()
	{
		return dbUser;
	}

	public static String dbPassword()
	{
		return dbPassword;
	}

	public static String dbUrl()
	{
		return String.format("jdbc:mysql://%s:%s/%s",
			dbHost,
			dbPort,
			dbBase
		);
	}
}
