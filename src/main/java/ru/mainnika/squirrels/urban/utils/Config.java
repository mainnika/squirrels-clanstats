package ru.mainnika.squirrels.urban.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Config {

    private static String dbBase;
    private static String dbUser;
    private static String dbPassword;
    private static String dbHost;
    private static String dbPort;

    private static String serverIp;
    private static String serverPort;
    private static String serverSecret;

    private static String accountUid;
    private static String accountType;
    private static String accountKey;

    static {
        String dbBase = null;
        String dbUser = null;
        String dbPassword = null;
        String dbHost = null;
        String dbPort = null;

        String serverIp = null;
        String serverPort = null;
        String serverSecret = null;

        String accountUid = null;
        String accountType = null;
        String accountKey = null;

        try {
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

            Element server = (Element) config.getElementsByTagName("Server").item(0);
            serverIp = getTextValue(server, "Ip");
            serverPort = getTextValue(server, "Port");
            serverSecret = getTextValue(server, "Secret");

            Element account = (Element) config.getElementsByTagName("Account").item(0);
            accountUid = getTextValue(account, "Uid");
            accountType = getTextValue(account, "Type");
            accountKey = getTextValue(account, "Key");

        } catch (Exception e) {
            dbBase = "sqclanbase";
            dbUser = "root";
            dbPassword = "";
            dbHost = "localhost";
            dbPort = "3306";

            serverIp = "88.212.207.7";
            serverPort = "22227";
            serverSecret = "";

            accountUid = "8479389";
            accountType = "0";
            accountKey = "292e587617848804a15d6347ed80b1f6";
        } finally {
            Config.dbBase = dbBase;
            Config.dbUser = dbUser;
            Config.dbPassword = dbPassword;
            Config.dbHost = dbHost;
            Config.dbPort = dbPort;

            Config.serverIp = serverIp;
            Config.serverPort = serverPort;
            Config.serverSecret = serverSecret;

            Config.accountUid = accountUid;
            Config.accountType = accountType;
            Config.accountKey = accountKey;
        }
    }

    private static String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }

    public static String dbUser() {
        return dbUser;
    }

    public static String dbPassword() {
        return dbPassword;
    }

    public static String dbUrl() {
        return String.format("jdbc:mysql://%s:%s/%s",
                dbHost,
                dbPort,
                dbBase
        );
    }

    public static String serverIp() {
        return serverIp;
    }

    public static int serverPort() {
        return Integer.parseInt(serverPort);
    }

    public static String serverSecret() {
        return serverSecret;
    }

    public static long accountUid() {
        return Long.parseLong(accountUid);
    }

    public static int accountType() {
        return Integer.parseInt(accountType);
    }

    public static String accountKey() {
        return accountKey;
    }
}
