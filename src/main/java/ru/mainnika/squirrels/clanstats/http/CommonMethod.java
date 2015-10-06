package ru.mainnika.squirrels.clanstats.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class CommonMethod extends BaseMethod
{
	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		super.doGet(req, res);
	}
}
