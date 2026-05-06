import { http } from "@/services/http";
import type { WeatherResponse } from "@/types/weather";

export async function findWeatherByCity(city: string, accessToken: string) {
  const { data } = await http.get<WeatherResponse>("/api/weather", {
    params: { city },
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  return data;
}
