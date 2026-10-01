import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Booking } from '../models/booking';
import { BookingService } from '../services/booking.service';
import { Payment, PaymentService } from '../services/payment.service';

export interface PendingBooking {
  booking: Booking;
  venueName: string;
  venueLocation: string;
  days: number;
  rate: number;
  amount: number;
}

type Method = 'UPI' | 'CARD' | 'NETBANKING';

@Component({
  selector: 'app-payment',
  imports: [FormsModule, DatePipe, DecimalPipe, RouterLink],
  templateUrl: './payment.html'
})
export class PaymentPage implements OnInit {
  private bookingService = inject(BookingService);
  private paymentService = inject(PaymentService);

  pending = signal<PendingBooking | null>(null);
  method = signal<Method>('UPI');
  stage = signal<'form' | 'processing' | 'success'>('form');
  error = signal('');
  receipt = signal<Payment | null>(null);

  gst = computed(() => Math.round((this.pending()?.amount ?? 0) * 0.18));
  total = computed(() => (this.pending()?.amount ?? 0) + this.gst());

  upiId = '';
  cardNumber = '';
  cardName = '';
  expiry = '';
  cvv = '';
  bank = '';
  banks = ['State Bank of India', 'HDFC Bank', 'ICICI Bank', 'Axis Bank', 'Punjab National Bank', 'Kotak Mahindra Bank'];

  ngOnInit(): void {
    const data = history.state?.pending as PendingBooking | undefined;
    if (data) {
      this.pending.set(data);
    }
  }

  selectMethod(m: Method): void {
    this.method.set(m);
    this.error.set('');
  }

  private validate(): string | null {
    if (this.method() === 'UPI') {
      if (!/^[\w.\-]{2,}@[a-zA-Z]{2,}$/.test(this.upiId.trim())) {
        return 'Enter a valid UPI ID (e.g. name@okaxis)';
      }
    }
    if (this.method() === 'CARD') {
      if (!/^\d{16}$/.test(this.cardNumber.replace(/\s/g, ''))) return 'Card number must be 16 digits';
      if (this.cardName.trim().length < 3) return 'Enter the name on the card';
      if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(this.expiry.trim())) return 'Expiry must be in MM/YY format';
      if (!/^\d{3}$/.test(this.cvv)) return 'CVV must be 3 digits';
    }
    if (this.method() === 'NETBANKING' && !this.bank) {
      return 'Please select your bank';
    }
    return null;
  }

  pay(): void {
    const p = this.pending();
    if (!p) return;

    const err = this.validate();
    if (err) {
      this.error.set(err);
      return;
    }

    this.error.set('');
    this.stage.set('processing');

    // Simulate the payment gateway taking 2 seconds
    setTimeout(() => {
      this.bookingService.add(p.booking).subscribe({
        next: (saved) => {
          if (saved?.id) {
            this.completePayment(saved.id, p);
          } else {
            // Backend didn't return the id, so find the newest booking for this email
            this.bookingService.getAll().subscribe(all => {
              const mine = all.filter(b => b.customerEmail === p.booking.customerEmail);
              const latestId = Math.max(...mine.map(b => b.id ?? 0));
              this.completePayment(latestId, p);
            });
          }
        },
        error: (e) => {
          this.error.set(e.error?.message ?? 'Booking failed. You have not been charged.');
          this.stage.set('form');
        }
      });
    }, 2000);
  }

  private completePayment(bookingId: number, p: PendingBooking): void {
    const payment: Payment = {
      bookingId,
      transactionId: this.paymentService.newTransactionId(),
      amount: this.total(),
      method: this.method(),
      customerEmail: p.booking.customerEmail,
      paidAt: new Date().toISOString()
    };
    this.paymentService.save(payment);
    this.receipt.set(payment);
    this.stage.set('success');
  }

  print(): void {
    window.print();
  }
}