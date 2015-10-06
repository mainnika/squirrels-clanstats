package ru.mainnika.squirrels.clanstats.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseMethod extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		try
		{
			this.doMethod(req, res);
		} catch (ApiException err)
		{
			res.sendError(503);
		}
	}

	protected abstract void doMethod(HttpServletRequest req, HttpServletResponse res) throws ApiException, IOException;
}
