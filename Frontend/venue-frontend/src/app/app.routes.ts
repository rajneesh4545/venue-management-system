import { Routes } from '@angular/router';
import { VenueList } from './venue-list/venue-list';
import { BookingList } from './booking-list/booking-list';
import { Login } from './login/login';
import { Register } from './register/register';
import { Dashboard } from './dashboard/dashboard';
import { PaymentPage } from './payment/payment';
import { authGuard, customerGuard, guestGuard, managerGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Public pages
  { path: 'login', component: Login, canActivate: [guestGuard] },
  { path: 'register', component: Register, canActivate: [guestGuard] },

  // Manager only
  { path: 'dashboard', component: Dashboard, canActivate: [managerGuard] },

  // Customer only
  { path: 'payment', component: PaymentPage, canActivate: [customerGuard] },

  // Both roles
  { path: 'venues', component: VenueList, canActivate: [authGuard] },
  { path: 'bookings', component: BookingList, canActivate: [authGuard] },

  { path: '**', redirectTo: 'login' }
];