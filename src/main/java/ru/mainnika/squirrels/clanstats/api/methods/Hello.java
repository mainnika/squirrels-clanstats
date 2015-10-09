package ru.mainnika.squirrels.clanstats.api.methods;

import org.json.JSONException;
import org.json.JSONObject;
import ru.mainnika.squirrels.clanstats.api.ApiException;
import ru.mainnika.squirrels.clanstats.api.CommonJsonMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class Hello extends CommonJsonMethod
{
	@Override
	protected void doMethod(HttpServletRequest req, HttpJsonResponse res) throws ApiException, JSONException, IOException
	{
		JSONObject result = new JSONObject();
		res.sendJson(result);
	}
}
