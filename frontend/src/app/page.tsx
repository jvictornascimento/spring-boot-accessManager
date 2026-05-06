import { CloudSun, LockKeyhole, ShieldCheck } from "lucide-react";
import { AuthCard } from "@/components/auth-card";

export default function Home() {
  return (
    <main className="min-h-screen bg-[#090b0f] text-slate-50">
      <section className="mx-auto grid min-h-screen w-full max-w-6xl gap-10 px-5 py-8 md:grid-cols-[1fr_420px] md:items-center md:px-8">
        <div className="space-y-8">
          <div className="inline-flex items-center gap-3 rounded-md border border-orange-500/30 bg-orange-500/10 px-3 py-2 text-sm font-medium text-orange-200">
            <ShieldCheck className="h-4 w-4 text-orange-400" aria-hidden="true" />
            Bear Flow
          </div>

          <div className="max-w-2xl space-y-5">
            <h1 className="text-4xl font-semibold tracking-normal text-white md:text-6xl">
              bear-controlAccess-weather
            </h1>
            <p className="max-w-xl text-base leading-7 text-slate-300 md:text-lg">
              Acesso seguro com JWT, cadastro de usuarios e consulta de clima em uma experiencia direta para operacao.
            </p>
          </div>

          <div className="grid max-w-2xl gap-3 sm:grid-cols-2">
            <div className="rounded-md border border-slate-800 bg-slate-950/70 p-4">
              <LockKeyhole className="mb-3 h-5 w-5 text-orange-400" aria-hidden="true" />
              <h2 className="text-sm font-semibold text-white">Acesso controlado</h2>
              <p className="mt-2 text-sm leading-6 text-slate-400">
                Login com access token e refresh token protegido por cookie HttpOnly.
              </p>
            </div>
            <div className="rounded-md border border-slate-800 bg-slate-950/70 p-4">
              <CloudSun className="mb-3 h-5 w-5 text-orange-400" aria-hidden="true" />
              <h2 className="text-sm font-semibold text-white">Clima por cidade</h2>
              <p className="mt-2 text-sm leading-6 text-slate-400">
                Consulta autenticada da previsao atual via API do backend.
              </p>
            </div>
          </div>
        </div>

        <AuthCard />
      </section>
    </main>
  );
}
