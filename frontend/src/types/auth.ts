export type UserResponse = {
  id: number;
  name: string;
  email: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
};

export type LoginResponse = {
  authenticated: boolean;
  accessToken: string;
  user: UserResponse;
};

export type LoginRequest = {
  email: string;
  password: string;
};

export type RegisterRequest = {
  name: string;
  email: string;
  password: string;
};
