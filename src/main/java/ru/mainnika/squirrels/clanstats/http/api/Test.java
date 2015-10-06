package ru.mainnika.squirrels.clanstats.http.api;

import ru.mainnika.squirrels.clanstats.http.ApiException;
import ru.mainnika.squirrels.clanstats.http.SecuredMethod;
import ru.mainnika.squirrels.clanstats.storage.Database;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class Test extends SecuredMethod
{
	@Override
	protected void doMethod(HttpServletRequest req, HttpServletResponse res) throws ApiException, IOException
	{
		ServletOutputStream out = res.getOutputStream();

		int count = 0;

		try
		{
			Database.Guard guard = new Database.Guard();
			Database base = guard.get();

			count = base.test();

			guard.close();
		} catch (SQLException e)
		{
			throw new ApiException();
		}

		String result = String.format("{\"method\":\"test\", \"timestamp\": %d, \"result\": %d}", DateTime.getUnixtime(), count);
		res.addCookie(new Cookie("foo", "bar"));
		out.print(result);
	}
}
