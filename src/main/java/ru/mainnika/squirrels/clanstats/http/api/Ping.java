package ru.mainnika.squirrels.clanstats.http.api;

import ru.mainnika.squirrels.clanstats.http.ApiException;
import ru.mainnika.squirrels.clanstats.http.CommonMethod;
import ru.mainnika.squirrels.clanstats.utils.DateTime;
import ru.mainnika.squirrels.clanstats.utils.UnIdleService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Ping extends CommonMethod
{
	@Override
	protected void doMethod(HttpServletRequest req, HttpServletResponse res) throws ApiException, IOException
	{
		ServletOutputStream out = res.getOutputStream();

		UnIdleService.create().ping();

		String result = String.format("{\"method\":\"ping\", \"timestamp\": %d}", DateTime.getUnixtime());
		out.print(result);
	}
}
