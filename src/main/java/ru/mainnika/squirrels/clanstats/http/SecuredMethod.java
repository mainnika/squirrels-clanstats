package ru.mainnika.squirrels.clanstats.http;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class SecuredMethod extends BaseMethod
{
	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		Cookie[] cookies = req.getCookies();

		if (cookies.length > 0)
		{
			res.sendError(403);
			return;
		}

		super.doGet(req, res);
	}
}
