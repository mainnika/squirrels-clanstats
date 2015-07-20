package ru.mainnika.squirrels.clanstats.api;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Hello extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		ServletOutputStream out = resp.getOutputStream();

		String result = String.format("{\"method\":\"hello\", \"timestamp\": %d}", (int) (System.currentTimeMillis() / 1000L));

		out.print(result);
	}
}
