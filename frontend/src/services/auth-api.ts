import { http } from "@/services/http";
import type { LoginRequest, LoginResponse, RegisterRequest, UserResponse } from "@/types/auth";

export async function login(request: LoginRequest) {
  const { data } = await http.post<LoginResponse>("/api/auth/login", request);

  return data;
}

export async function registerUser(request: RegisterRequest) {
  const { data } = await http.post<UserResponse>("/api/users", request);

  return data;
}
