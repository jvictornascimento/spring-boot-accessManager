"use client";

import { CloudSun, LogIn, UserPlus } from "lucide-react";

export function AuthCard() {
  return (
    <section className="w-full rounded-lg border border-slate-800 bg-slate-950 p-5 shadow-2xl shadow-black/30">
      <div className="mb-6 flex items-center justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-orange-300">Entrar</p>
          <h2 className="mt-1 text-2xl font-semibold text-white">Acesse sua conta</h2>
        </div>
        <div className="grid h-11 w-11 place-items-center rounded-md bg-orange-500 text-white">
          <CloudSun className="h-5 w-5" aria-hidden="true" />
        </div>
      </div>

      <form className="space-y-4">
        <label className="block">
          <span className="text-sm font-medium text-slate-300">Email</span>
          <input
            className="mt-2 h-11 w-full rounded-md border border-slate-700 bg-slate-900 px-3 text-sm text-white placeholder:text-slate-500"
            type="email"
            placeholder="admin@bearflow.local"
            autoComplete="email"
          />
        </label>

        <label className="block">
          <span className="text-sm font-medium text-slate-300">Senha</span>
          <input
            className="mt-2 h-11 w-full rounded-md border border-slate-700 bg-slate-900 px-3 text-sm text-white placeholder:text-slate-500"
            type="password"
            placeholder="Sua senha"
            autoComplete="current-password"
          />
        </label>

        <button
          className="inline-flex h-11 w-full items-center justify-center gap-2 rounded-md bg-orange-500 px-4 text-sm font-semibold text-white transition hover:bg-orange-600"
          type="button"
        >
          <LogIn className="h-4 w-4" aria-hidden="true" />
          Entrar
        </button>
      </form>

      <button
        className="mt-4 inline-flex h-11 w-full items-center justify-center gap-2 rounded-md border border-slate-700 px-4 text-sm font-semibold text-slate-100 transition hover:border-orange-500/70 hover:text-orange-200"
        type="button"
      >
        <UserPlus className="h-4 w-4" aria-hidden="true" />
        Criar cadastro
      </button>
    </section>
  );
}
