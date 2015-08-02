package ru.mainnika.squirrels.clanstats.storage;

import ru.mainnika.squirrels.clanstats.analytics.AnalyticSnapshot;
import ru.mainnika.squirrels.clanstats.analytics.AnalyticSnapshot.Snapshot;
import ru.mainnika.squirrels.clanstats.utils.Config;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

public class Database
{
	public static class Guard
	{
		private Database instance;

		public Guard() throws SQLException
		{
			this.instance = available.pollFirst();

			if (this.instance == null)
			{
				log.info("Creating new Database instance");
				this.instance = new Database();
				return;
			}

			if (this.instance.sql.isClosed())
			{
				log.info("Creating new Database instance");
				this.instance = new Database();
				return;
			}

			log.info("Getting existing Database instance");
		}

		public Database get() throws IOException
		{
			if (this.instance == null)
				throw new IOException("Guard already closed");

			return this.instance;
		}

		public void close()
		{
			if (this.instance == null)
				return;

			available.addLast(this.instance);
			this.instance = null;
		}
	}

	private static final Logger log;
	private static final String url;
	private static final String user;
	private static final String password;

	private static final ConcurrentLinkedDeque<Database> available;

	static
	{
		log = Logger.getLogger(Savable.class.getName());

		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ignored)
		{
		}

		user = Config.dbUser();
		password = Config.dbPassword();
		url = Config.dbUrl();

		available = new ConcurrentLinkedDeque<>();
	}

	private Connection sql;

	private Database() throws SQLException
	{
		this.sql = DriverManager.getConnection(url, user, password);
	}

	public int test() throws SQLException
	{
		log.info("Execute test");

		String query = "SELECT COUNT(*) as `count` from `snapshots`";
		PreparedStatement statement = this.sql.prepareStatement(query);

		ResultSet result = statement.executeQuery();

		result.first();

		return result.getInt("count");
	}

	public void saveSnapshot(AnalyticSnapshot snapshot) throws SQLException
	{
		log.info("Saving snapshots " + snapshot.name());

		String query = "REPLACE INTO `snapshots` (`hash`, `type`, `id`, `data`, `value`) VALUES ";
		String values = "";

		List<Snapshot> unsaved = snapshot.getUnsaved();

		for (int i = 0; i < unsaved.size(); i++)
		{
			Snapshot element = unsaved.get(i);

			if (i > 0)
			{
				values += ",";
			}

			values += String.format("(%d, %d, %d, %d, %d)",
				element.hash,
				element.type,
				element.id,
				element.data,
				element.value
			);
		}

		Statement statement = this.sql.createStatement();
		int saved = statement.executeUpdate(query + values);
		log.info("Saved snapshots: " + Integer.toString(saved));

		snapshot.clearUnsaved();
	}
}
