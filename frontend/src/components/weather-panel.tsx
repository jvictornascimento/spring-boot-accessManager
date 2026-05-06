"use client";

import { FormEvent, useState } from "react";
import { CloudSun, Loader2, MapPin, Search } from "lucide-react";
import { getApiErrorMessage } from "@/services/api-error";
import { findWeatherByCity } from "@/services/weather-api";
import type { WeatherResponse } from "@/types/weather";

type WeatherPanelProps = {
  accessToken: string;
  userName: string;
};

export function WeatherPanel({ accessToken, userName }: WeatherPanelProps) {
  const [city, setCity] = useState("");
  const [weather, setWeather] = useState<WeatherResponse | null>(null);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      const response = await findWeatherByCity(city.trim(), accessToken);
      setWeather(response);
    } catch (requestError) {
      setWeather(null);
      setError(getApiErrorMessage(requestError, "Nao foi possivel consultar o clima."));
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <section className="rounded-lg border border-slate-800 bg-slate-950 p-5 shadow-2xl shadow-black/30">
      <div className="mb-6 flex items-center justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-orange-300">Sessao ativa</p>
          <h2 className="mt-1 text-2xl font-semibold text-white">{userName}</h2>
        </div>
        <div className="grid h-11 w-11 place-items-center rounded-md bg-orange-500 text-white">
          <CloudSun className="h-5 w-5" aria-hidden="true" />
        </div>
      </div>

      <form className="space-y-4" onSubmit={handleSubmit}>
        <label className="block">
          <span className="text-sm font-medium text-slate-300">Cidade</span>
          <input
            className="mt-2 h-11 w-full rounded-md border border-slate-700 bg-slate-900 px-3 text-sm text-white placeholder:text-slate-500"
            type="text"
            placeholder="Recife"
            value={city}
            onChange={(event) => {
              setCity(event.target.value);
              setError("");
            }}
            required
          />
        </label>

        {error ? (
          <p className="rounded-md border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-200" role="alert">
            {error}
          </p>
        ) : null}

        <button
          className="inline-flex h-11 w-full items-center justify-center gap-2 rounded-md bg-orange-500 px-4 text-sm font-semibold text-white transition hover:bg-orange-600 disabled:cursor-not-allowed disabled:opacity-70"
          type="submit"
          disabled={isLoading}
        >
          {isLoading ? <Loader2 className="h-4 w-4 animate-spin" aria-hidden="true" /> : <Search className="h-4 w-4" aria-hidden="true" />}
          Consultar clima
        </button>
      </form>

      {weather ? (
        <div className="mt-5 rounded-md border border-slate-800 bg-slate-900 p-4">
          <div className="flex items-start justify-between gap-4">
            <div>
              <p className="inline-flex items-center gap-2 text-sm font-medium text-orange-200">
                <MapPin className="h-4 w-4" aria-hidden="true" />
                {weather.city}, {weather.country}
              </p>
              <p className="mt-2 text-4xl font-semibold text-white">{Math.round(weather.temperature)}°C</p>
              <p className="mt-1 text-sm capitalize text-slate-300">{weather.description}</p>
            </div>
            <div className="text-right text-sm text-slate-400">
              <p>Sensacao {Math.round(weather.feelsLike)}°C</p>
              <p>Umidade {weather.humidity}%</p>
              <p>Vento {weather.windSpeed} m/s</p>
            </div>
          </div>
        </div>
      ) : null}
    </section>
  );
}
