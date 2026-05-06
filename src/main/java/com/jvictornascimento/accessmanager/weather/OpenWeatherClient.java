package com.jvictornascimento.accessmanager.weather;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openWeatherClient", url = "${openweather.api.url}", primary = false)
public interface OpenWeatherClient {

	@GetMapping("/data/2.5/weather")
	OpenWeatherCurrentResponse getCurrentWeather(
		@RequestParam("q") String city,
		@RequestParam("appid") String apiKey,
		@RequestParam("units") String units,
		@RequestParam("lang") String lang
	);

}
