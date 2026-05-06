"use client";

import { FormEvent, useState } from "react";
import { CheckCircle2, CloudSun, Loader2, LogIn, UserPlus } from "lucide-react";
import { getApiErrorMessage } from "@/services/api-error";
import { login, registerUser } from "@/services/auth-api";
import type { LoginResponse } from "@/types/auth";

type AuthMode = "login" | "register";

type AuthCardProps = {
  onAuthenticated?: (response: LoginResponse) => void;
};

const initialForm = {
  name: "",
  email: "",
  password: "",
};

export function AuthCard({ onAuthenticated }: AuthCardProps) {
  const [mode, setMode] = useState<AuthMode>("login");
  const [form, setForm] = useState(initialForm);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const isRegister = mode === "register";

  function updateField(field: keyof typeof initialForm, value: string) {
    setForm((current) => ({ ...current, [field]: value }));
    setError("");
    setSuccess("");
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsLoading(true);
    setError("");
    setSuccess("");

    try {
      if (isRegister) {
        await registerUser({
          name: form.name.trim(),
          email: form.email.trim(),
          password: form.password,
        });
        setSuccess("Cadastro criado. Entre com o email e senha cadastrados.");
        setMode("login");
        setForm((current) => ({ ...current, name: "", password: "" }));
        return;
      }

      const response = await login({
        email: form.email.trim(),
        password: form.password,
      });
      setSuccess("Login realizado.");
      onAuthenticated?.(response);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Nao foi possivel concluir a operacao."));
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <section className="w-full rounded-lg border border-slate-800 bg-slate-950 p-5 shadow-2xl shadow-black/30">
      <div className="mb-6 flex items-center justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-orange-300">{isRegister ? "Cadastro" : "Entrar"}</p>
          <h2 className="mt-1 text-2xl font-semibold text-white">
            {isRegister ? "Crie seu acesso" : "Acesse sua conta"}
          </h2>
        </div>
        <div className="grid h-11 w-11 place-items-center rounded-md bg-orange-500 text-white">
          <CloudSun className="h-5 w-5" aria-hidden="true" />
        </div>
      </div>

      <form className="space-y-4" onSubmit={handleSubmit}>
        {isRegister ? (
          <label className="block">
            <span className="text-sm font-medium text-slate-300">Nome</span>
            <input
              className="mt-2 h-11 w-full rounded-md border border-slate-700 bg-slate-900 px-3 text-sm text-white placeholder:text-slate-500"
              type="text"
              placeholder="Jane Doe"
              autoComplete="name"
              value={form.name}
              onChange={(event) => updateField("name", event.target.value)}
              required
              maxLength={120}
            />
          </label>
        ) : null}

        <label className="block">
          <span className="text-sm font-medium text-slate-300">Email</span>
          <input
            className="mt-2 h-11 w-full rounded-md border border-slate-700 bg-slate-900 px-3 text-sm text-white placeholder:text-slate-500"
            type="email"
            placeholder="admin@bearflow.local"
            autoComplete="email"
            value={form.email}
            onChange={(event) => updateField("email", event.target.value)}
            required
            maxLength={160}
          />
        </label>

        <label className="block">
          <span className="text-sm font-medium text-slate-300">Senha</span>
          <input
            className="mt-2 h-11 w-full rounded-md border border-slate-700 bg-slate-900 px-3 text-sm text-white placeholder:text-slate-500"
            type="password"
            placeholder="Sua senha"
            autoComplete={isRegister ? "new-password" : "current-password"}
            value={form.password}
            onChange={(event) => updateField("password", event.target.value)}
            required
            minLength={8}
            maxLength={100}
          />
        </label>

        {error ? (
          <p className="rounded-md border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-200" role="alert">
            {error}
          </p>
        ) : null}

        {success ? (
          <p className="inline-flex w-full items-center gap-2 rounded-md border border-emerald-500/30 bg-emerald-500/10 px-3 py-2 text-sm text-emerald-200">
            <CheckCircle2 className="h-4 w-4" aria-hidden="true" />
            {success}
          </p>
        ) : null}

        <button
          className="inline-flex h-11 w-full items-center justify-center gap-2 rounded-md bg-orange-500 px-4 text-sm font-semibold text-white transition hover:bg-orange-600 disabled:cursor-not-allowed disabled:opacity-70"
          type="submit"
          disabled={isLoading}
        >
          {isLoading ? (
            <Loader2 className="h-4 w-4 animate-spin" aria-hidden="true" />
          ) : isRegister ? (
            <UserPlus className="h-4 w-4" aria-hidden="true" />
          ) : (
            <LogIn className="h-4 w-4" aria-hidden="true" />
          )}
          {isRegister ? "Criar cadastro" : "Entrar"}
        </button>
      </form>

      <button
        className="mt-4 inline-flex h-11 w-full items-center justify-center gap-2 rounded-md border border-slate-700 px-4 text-sm font-semibold text-slate-100 transition hover:border-orange-500/70 hover:text-orange-200"
        type="button"
        onClick={() => {
          setMode(isRegister ? "login" : "register");
          setError("");
          setSuccess("");
        }}
      >
        {isRegister ? <LogIn className="h-4 w-4" aria-hidden="true" /> : <UserPlus className="h-4 w-4" aria-hidden="true" />}
        {isRegister ? "Ja tenho conta" : "Criar cadastro"}
      </button>
    </section>
  );
}
