import { Injectable, computed, signal } from '@angular/core';
import { User } from '../models/user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private usersKey = 'vv_users';
  private sessionKey = 'vv_current_user';

  currentUser = signal<User | null>(this.loadSession());
  isLoggedIn = computed(() => this.currentUser() !== null);
  isManager = computed(() => this.currentUser()?.role === 'MANAGER');

  constructor() {
    this.seedDemoAccounts();
  }

  register(user: User): string | null {
    const users = this.getUsers();
    const exists = users.some(u => u.email.toLowerCase() === user.email.toLowerCase());
    if (exists) {
      return 'An account with this email already exists';
    }
    users.push(user);
    localStorage.setItem(this.usersKey, JSON.stringify(users));
    return null;
  }

  login(email: string, password: string): string | null {
    const user = this.getUsers().find(
      u => u.email.toLowerCase() === email.toLowerCase() && u.password === password
    );
    if (!user) {
      return 'Invalid email or password';
    }
    const sessionUser: User = { name: user.name, email: user.email, role: user.role };
    localStorage.setItem(this.sessionKey, JSON.stringify(sessionUser));
    this.currentUser.set(sessionUser);
    return null;
  }

  logout(): void {
    localStorage.removeItem(this.sessionKey);
    this.currentUser.set(null);
  }

  homeRoute(): string {
    return this.isManager() ? '/dashboard' : '/venues';
  }

  private getUsers(): User[] {
    try {
      return JSON.parse(localStorage.getItem(this.usersKey) || '[]');
    } catch {
      return [];
    }
  }

  private loadSession(): User | null {
    try {
      return JSON.parse(localStorage.getItem(this.sessionKey) || 'null');
    } catch {
      return null;
    }
  }

  private seedDemoAccounts(): void {
    if (this.getUsers().length > 0) return;
    const demo: User[] = [
      { name: 'Event Manager', email: 'manager@venuevista.com', password: 'manager123', role: 'MANAGER' },
      { name: 'Demo Customer', email: 'customer@venuevista.com', password: 'customer123', role: 'CUSTOMER' }
    ];
    localStorage.setItem(this.usersKey, JSON.stringify(demo));
  }
}