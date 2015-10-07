package ru.mainnika.squirrels.clanstats.http;

import org.json.JSONException;
import org.json.JSONObject;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public abstract class JsonMethod extends HttpServlet
{
	public class HttpJsonResponse extends HttpServletResponseWrapper
	{
		public HttpJsonResponse(HttpServletResponse base)
		{
			super(base);
		}

		public final void sendJson(JSONObject json) throws IOException, JSONException
		{
			this.setContentType("application/json");

			json.put("method", JsonMethod.this.getClass().getSimpleName().toLowerCase());
			json.put("timestamp", DateTime.getUnixtime());

			ServletOutputStream out = this.getOutputStream();
			out.print(json.toString());
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		try
		{
			this.doMethod(req, new HttpJsonResponse(res));
		} catch (ApiException | JSONException err)
		{
			res.sendError(503);
		}
	}

	protected abstract void doMethod(HttpServletRequest req, HttpJsonResponse res) throws ApiException, JSONException, IOException;
}
