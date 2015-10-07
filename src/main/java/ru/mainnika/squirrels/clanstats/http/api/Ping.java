package ru.mainnika.squirrels.clanstats.http.api;

import org.json.JSONException;
import org.json.JSONObject;
import ru.mainnika.squirrels.clanstats.http.ApiException;
import ru.mainnika.squirrels.clanstats.http.CommonJsonMethod;
import ru.mainnika.squirrels.clanstats.utils.UnIdleService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class Ping extends CommonJsonMethod
{
	@Override
	protected void doMethod(HttpServletRequest req, HttpJsonResponse res) throws ApiException, JSONException, IOException
	{
		UnIdleService.create().ping();

		JSONObject result = new JSONObject();
		res.sendJson(result);
	}
}
