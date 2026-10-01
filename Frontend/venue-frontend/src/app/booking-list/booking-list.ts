import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Booking } from '../models/booking';
import { Venue } from '../models/venue';
import { BookingService } from '../services/booking.service';
import { VenueService } from '../services/venue.service';
import { PaymentService } from '../services/payment.service';
import { AuthService } from '../services/auth.service';
import { PendingBooking } from '../payment/payment';

@Component({
  selector: 'app-booking-list',
  imports: [FormsModule, DecimalPipe, RouterLink],
  templateUrl: './booking-list.html'
})
export class BookingList implements OnInit {
  private bookingService = inject(BookingService);
  private venueService = inject(VenueService);
  private paymentService = inject(PaymentService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  auth = inject(AuthService);

  bookings = signal<Booking[]>([]);
  venues = signal<Venue[]>([]);
  loading = signal(true);
  error = signal('');
  search = signal('');

  customerName = this.auth.currentUser()?.name ?? '';
  customerEmail = this.auth.currentUser()?.email ?? '';
  startDate = '';
  endDate = '';
  venueId: number | null = null;
  today = new Date().toISOString().slice(0, 10);

  visibleBookings = computed(() => {
    const user = this.auth.currentUser();
    let list = [...this.bookings()].sort((a, b) => (b.id ?? 0) - (a.id ?? 0));

    if (!this.auth.isManager()) {
      list = list.filter(b => b.customerEmail?.toLowerCase() === user?.email.toLowerCase());
    }

    const q = this.search().toLowerCase().trim();
    if (q) {
      list = list.filter(b =>
        b.customerName.toLowerCase().includes(q) ||
        b.customerEmail.toLowerCase().includes(q) ||
        (b.venue?.name ?? '').toLowerCase().includes(q)
      );
    }
    return list;
  });

  ngOnInit(): void {
    const id = this.route.snapshot.queryParamMap.get('venueId');
    if (id) {
      this.venueId = Number(id);
    }

    this.loadBookings();
    this.venueService.getAll().subscribe({
      next: (data) => this.venues.set(data),
      error: () => this.error.set('Could not load venues for the dropdown')
    });
  }

  loadBookings(): void {
    this.loading.set(true);
    this.bookingService.getAll().subscribe({
      next: (data) => {
        this.bookings.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load bookings. Is the backend running?');
        this.loading.set(false);
      }
    });
  }

  selectedVenue(): Venue | undefined {
    return this.venues().find(v => v.id === this.venueId);
  }

  days(): number {
    return this.paymentService.countDays(this.startDate, this.endDate);
  }

  rate(): number {
    return this.paymentService.dailyRate(this.selectedVenue()?.capacity);
  }

  subtotal(): number {
    return this.days() * this.rate();
  }

  paymentFor(b: Booking) {
    return this.paymentService.getByBookingId(b.id);
  }

  proceedToPayment(): void {
    const venue = this.selectedVenue();

    if (!venue) {
      this.error.set('Please select a venue');
      return;
    }
    if (!this.startDate || !this.endDate) {
      this.error.set('Please select both start and end dates');
      return;
    }
    if (this.startDate < this.today) {
      this.error.set('Start date cannot be in the past');
      return;
    }
    if (this.days() === 0) {
      this.error.set('End date must be on or after the start date');
      return;
    }

    const pending: PendingBooking = {
      booking: {
        customerName: this.customerName,
        customerEmail: this.customerEmail,
        startDate: this.startDate,
        endDate: this.endDate,
        venue: { id: venue.id }
      },
      venueName: venue.name,
      venueLocation: venue.location,
      days: this.days(),
      rate: this.rate(),
      amount: this.subtotal()
    };

    this.router.navigateByUrl('/payment', { state: { pending } });
  }

  deleteBooking(id: number): void {
    const message = this.auth.isManager()
      ? 'Are you sure you want to delete this booking?'
      : 'Are you sure you want to cancel this booking?';
    if (!confirm(message)) return;

    this.bookingService.delete(id).subscribe({
      next: () => this.loadBookings(),
      error: () => this.error.set('Could not delete booking')
    });
  }
}