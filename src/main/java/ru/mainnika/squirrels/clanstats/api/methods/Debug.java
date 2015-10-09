package ru.mainnika.squirrels.clanstats.api.methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.mainnika.squirrels.clanstats.Main;
import ru.mainnika.squirrels.clanstats.api.ApiException;
import ru.mainnika.squirrels.clanstats.api.CommonJsonMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Debug extends CommonJsonMethod
{
	@Override
	protected void doMethod(HttpServletRequest req, HttpJsonResponse res) throws ApiException, JSONException, IOException
	{
		List<String> debug = Arrays.asList(Main.getVersion().split("\n"));
		JSONObject result = new JSONObject();

		result.put("result", new JSONArray(debug));

		res.sendJson(result);
	}
}
