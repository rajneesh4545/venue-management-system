import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Role } from '../models/user';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html'
})
export class Register {
  private auth = inject(AuthService);
  private router = inject(Router);

  name = '';
  email = '';
  password = '';
  confirmPassword = '';
  role: Role = 'CUSTOMER';
  showPassword = false;
  error = signal('');

  register(): void {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (this.name.trim().length < 3) {
      this.error.set('Name must be at least 3 characters');
      return;
    }
    if (!emailPattern.test(this.email.trim())) {
      this.error.set('Please enter a valid email');
      return;
    }
    if (this.password.length < 6) {
      this.error.set('Password must be at least 6 characters');
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.error.set('Passwords do not match');
      return;
    }

    const err = this.auth.register({
      name: this.name.trim(),
      email: this.email.trim(),
      password: this.password,
      role: this.role
    });
    if (err) {
      this.error.set(err);
      return;
    }

    this.auth.login(this.email.trim(), this.password);
    this.router.navigateByUrl(this.auth.homeRoute());
  }
}