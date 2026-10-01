import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Venue } from '../models/venue';
import { VenueService } from '../services/venue.service';
import { AuthService } from '../services/auth.service';
import { PaymentService } from '../services/payment.service';

@Component({
  selector: 'app-venue-list',
  imports: [FormsModule, DecimalPipe, RouterLink],
  templateUrl: './venue-list.html'
})
export class VenueList implements OnInit {
  private venueService = inject(VenueService);
  private paymentService = inject(PaymentService);
  auth = inject(AuthService);

  venues = signal<Venue[]>([]);
  loading = signal(true);
  error = signal('');
  success = signal('');
  search = signal('');
  showForm = signal(false);
  saving = signal(false);

  newName = '';
  newLocation = '';
  newCapacity: number | null = null;

  filteredVenues = computed(() => {
    const q = this.search().toLowerCase().trim();
    if (!q) return this.venues();
    return this.venues().filter(v =>
      v.name.toLowerCase().includes(q) || v.location.toLowerCase().includes(q)
    );
  });

  ngOnInit(): void {
    this.loadVenues();
  }

  loadVenues(): void {
    this.loading.set(true);
    this.venueService.getAll().subscribe({
      next: (data) => {
        this.venues.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load venues. Is the backend running?');
        this.loading.set(false);
      }
    });
  }

  dailyRate(v: Venue): number {
    return this.paymentService.dailyRate(v.capacity);
  }

  addVenue(): void {
    if (this.newName.trim().length < 3) {
      this.error.set('Venue name must be at least 3 characters');
      return;
    }
    if (!this.newLocation.trim()) {
      this.error.set('Please enter a location');
      return;
    }
    if (!this.newCapacity || this.newCapacity < 1) {
      this.error.set('Capacity must be at least 1');
      return;
    }

    this.saving.set(true);
    this.error.set('');

    this.venueService.add({
      name: this.newName.trim(),
      location: this.newLocation.trim(),
      capacity: this.newCapacity
    }).subscribe({
      next: () => {
        this.success.set(`Venue "${this.newName.trim()}" added successfully!`);
        this.newName = '';
        this.newLocation = '';
        this.newCapacity = null;
        this.showForm.set(false);
        this.saving.set(false);
        this.loadVenues();
        setTimeout(() => this.success.set(''), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message ?? 'Could not add venue');
        this.saving.set(false);
      }
    });
  }
}