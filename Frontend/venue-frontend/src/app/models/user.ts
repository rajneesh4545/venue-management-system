export type Role = 'CUSTOMER' | 'MANAGER';

export interface User {
  name: string;
  email: string;
  password?: string;
  role: Role;
}