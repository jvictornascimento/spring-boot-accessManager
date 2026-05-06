import { AxiosError } from "axios";

type ApiErrorResponse = {
  message?: string;
  error?: string;
};

export function getApiErrorMessage(error: unknown, fallback: string) {
  if (error instanceof AxiosError) {
    const data = error.response?.data as ApiErrorResponse | undefined;

    return data?.message ?? data?.error ?? fallback;
  }

  return fallback;
}
