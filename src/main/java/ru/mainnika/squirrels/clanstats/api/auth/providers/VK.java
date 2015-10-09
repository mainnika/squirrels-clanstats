package ru.mainnika.squirrels.clanstats.api.auth.providers;

import ru.mainnika.squirrels.clanstats.api.auth.providers.$.AbstractProvider;
import ru.mainnika.squirrels.clanstats.utils.Config;

public class VK implements AbstractProvider
{
	static String loginUrlPattern = "https://oauth.vk.com/authorize?client_id=%s&scope=%s&redirect_uri=%s&display=page&revoke=1&response_type=code&v=5.37";
	static String scope = "wall";

	static String id;
	static String secure;
	static String redirect;

	static
	{
		id = Config.socialSetting("vk", "id");
		secure = Config.socialSetting("vk", "secure");
		redirect = Config.socialSetting("vk", "redirect");
	}

	@Override
	public String redirectUrl()
	{
		return redirect;
	}

	@Override
	public String loginUrl()
	{
		return String.format(loginUrlPattern, id, scope, redirect);
	}
}
