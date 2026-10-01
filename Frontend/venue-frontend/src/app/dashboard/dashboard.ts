import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Venue } from '../models/venue';
import { Booking } from '../models/booking';
import { VenueService } from '../services/venue.service';
import { BookingService } from '../services/booking.service';
import { PaymentService } from '../services/payment.service';

@Component({
  selector: 'app-dashboard',
  imports: [DecimalPipe, RouterLink],
  templateUrl: './dashboard.html'
})
export class Dashboard implements OnInit {
  private venueService = inject(VenueService);
  private bookingService = inject(BookingService);
  private paymentService = inject(PaymentService);

  venues = signal<Venue[]>([]);
  bookings = signal<Booking[]>([]);
  loading = signal(true);
  error = signal('');

  totalCapacity = computed(() =>
    this.venues().reduce((sum, v) => sum + (v.capacity ?? 0), 0)
  );

  revenue = computed(() =>
    this.bookings().reduce((sum, b) => sum + (this.paymentService.getByBookingId(b.id)?.amount ?? 0), 0)
  );

  paidCount = computed(() =>
    this.bookings().filter(b => this.paymentService.getByBookingId(b.id)).length
  );

  recentBookings = computed(() =>
    [...this.bookings()].sort((a, b) => (b.id ?? 0) - (a.id ?? 0)).slice(0, 5)
  );

  topVenues = computed(() => {
    const counts = new Map<string, number>();
    for (const b of this.bookings()) {
      const name = b.venue?.name ?? 'Unknown';
      counts.set(name, (counts.get(name) ?? 0) + 1);
    }
    const max = Math.max(1, ...counts.values());
    return [...counts.entries()]
      .map(([name, count]) => ({ name, count, percent: Math.round((count / max) * 100) }))
      .sort((a, b) => b.count - a.count)
      .slice(0, 5);
  });

  ngOnInit(): void {
    forkJoin({
      venues: this.venueService.getAll(),
      bookings: this.bookingService.getAll()
    }).subscribe({
      next: ({ venues, bookings }) => {
        this.venues.set(venues);
        this.bookings.set(bookings);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load dashboard data. Is the backend running?');
        this.loading.set(false);
      }
    });
  }

  isPaid(b: Booking): boolean {
    return !!this.paymentService.getByBookingId(b.id);
  }
}
