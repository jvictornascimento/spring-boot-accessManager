import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Bear Flow Access Weather",
  description: "Controle de acesso e consulta de clima Bear Flow.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-BR">
      <body>{children}</body>
    </html>
  );
}
