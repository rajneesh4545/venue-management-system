import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html'
})
export class Login {
  private auth = inject(AuthService);
  private router = inject(Router);

  email = '';
  password = '';
  showPassword = false;
  error = signal('');

  fillDemo(role: 'MANAGER' | 'CUSTOMER'): void {
    if (role === 'MANAGER') {
      this.email = 'manager@venuevista.com';
      this.password = 'manager123';
    } else {
      this.email = 'customer@venuevista.com';
      this.password = 'customer123';
    }
    this.error.set('');
  }

  login(): void {
    const err = this.auth.login(this.email.trim(), this.password);
    if (err) {
      this.error.set(err);
      return;
    }
    this.router.navigateByUrl(this.auth.homeRoute());
  }
}