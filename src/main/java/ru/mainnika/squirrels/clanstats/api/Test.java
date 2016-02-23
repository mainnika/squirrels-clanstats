package ru.mainnika.squirrels.clanstats.api;

import ru.mainnika.squirrels.clanstats.storage.Database;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class Test extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		ServletOutputStream out = resp.getOutputStream();
		int count = 0;

		try
		{
			Database.Guard guard = new Database.Guard();
			Database base = guard.get();

			count = base.test();

			guard.close();
		} catch (SQLException e)
		{
			throw new IOException(e);
		}

		String result = String.format("{\"method\":\"test\", \"timestamp\": %d, \"result\": %d}", DateTime.getUnixtime(), count);

		out.print(result);
	}
}
