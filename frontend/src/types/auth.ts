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
