package ru.mainnika.squirrels.clanstats.api.methods;

import org.json.JSONException;
import org.json.JSONObject;
import ru.mainnika.squirrels.clanstats.api.ApiException;
import ru.mainnika.squirrels.clanstats.api.SecuredJsonMethod;
import ru.mainnika.squirrels.clanstats.storage.Database;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

public class Test extends SecuredJsonMethod
{
	@Override
	protected void doMethod(HttpServletRequest req, HttpJsonResponse res) throws ApiException, JSONException, IOException
	{
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

		JSONObject result = new JSONObject();

		result.put("result", count);

		res.addCookie(new Cookie("foo", "bar"));
		res.sendJson(result);
	}
}
