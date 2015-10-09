package ru.mainnika.squirrels.clanstats.utils;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class Config
{
	private static String dbBase;
	private static String dbUser;
	private static String dbPassword;
	private static String dbHost;
	private static String dbPort;

	private static MultiKeyMap<String, String> socialCredentials;

	static
	{
		String dbBase = null;
		String dbUser = null;
		String dbPassword = null;
		String dbHost = null;
		String dbPort = null;

		MultiKeyMap<String, String> socialCredentials = new MultiKeyMap<>();

		try
		{
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(loader.getResource("config.xml").getFile()));

			Element config = doc.getDocumentElement();

			Element dbStorage = (Element) config.getElementsByTagName("Storage").item(0);
			dbBase = getTextValue(dbStorage, "Database");
			dbUser = getTextValue(dbStorage, "User");
			dbPassword = getTextValue(dbStorage, "Password");
			dbHost = getTextValue(dbStorage, "Host");
			dbPort = getTextValue(dbStorage, "Port");

			Element socialStorage = (Element) config.getElementsByTagName("Social").item(0);
			ArrayList<Element> socialElements = getElementChilds(socialStorage);
			for (Element socialElement : socialElements)
			{
				ArrayList<Element> social = getElementChilds(socialElement);
				String socialName = socialElement.getNodeName().toLowerCase();
				for (Element socialSetting : social)
				{
					String setting = socialSetting.getNodeName().toLowerCase();
					String value = socialSetting.getFirstChild().getNodeValue().toLowerCase();

					socialCredentials.put(socialName, setting, value);
				}
			}

		} catch (Exception e)
		{
			dbBase = "sqclanbase";
			dbUser = "root";
			dbPassword = "";
			dbHost = "localhost";
			dbPort = "3306";

			socialCredentials.clear();
		} finally
		{
			Config.dbBase = dbBase;
			Config.dbUser = dbUser;
			Config.dbPassword = dbPassword;
			Config.dbHost = dbHost;
			Config.dbPort = dbPort;

			Config.socialCredentials = socialCredentials;
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

	private static ArrayList<Element> getElementChilds(Element element)
	{
		ArrayList<Element> result = new ArrayList<>();
		NodeList childs = element.getChildNodes();

		for (int i = 0; i < childs.getLength(); i++)
		{
			Object childObject = childs.item(i);
			if (!(childObject instanceof Element))
				continue;

			result.add((Element)childObject);
		}

		return result;
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

	public static String socialSetting(String socialName, String setting)
	{
		return socialCredentials.get(socialName, setting);
	}
}
