package ru.mainnika.squirrels.clanstats.api.methods;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONException;
import ru.mainnika.squirrels.clanstats.api.ApiException;
import ru.mainnika.squirrels.clanstats.api.CommonJsonMethod;
import ru.mainnika.squirrels.clanstats.api.auth.providers.$.AbstractProvider;
import ru.mainnika.squirrels.clanstats.api.auth.providers.VK;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

public class Auth extends CommonJsonMethod
{
	private static HashMap<String, AbstractProvider> providers;

	static
	{
		providers = new HashMap<>();

		providers.put("vk", new VK());
	}

	@Override
	protected void doMethod(HttpServletRequest req, HttpJsonResponse res) throws ApiException, JSONException, IOException
	{
		String path = req.getPathInfo();

		if (path == null)
		{
			throw new ApiException();
		}

		String[] raw = path.split("/");

		if (raw.length <= 2)
		{
			throw new ApiException();
		}

		String[] args = ArrayUtils.subarray(raw, 3, raw.length);
		String command = raw[2].toLowerCase();
		String social = raw[1].toLowerCase();
		AbstractProvider provider = providers.get(social);

		switch (command)
		{
			case "login":
				login(provider, res);
				break;
			case "redirect":
//				redirect(args);
				break;
			case "complete":
//				complete(args);
				break;

			default:
				throw new ApiException();
		}
	}

	private void login(AbstractProvider provider, HttpJsonResponse res) throws IOException
	{
		String to_login = provider.loginUrl();
		res.sendRedirect(to_login);
	}

	private void redirect(String[] args)
	{

	}

	private void complete(String[] args)
	{

	}
}
