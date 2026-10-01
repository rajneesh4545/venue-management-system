import { Injectable } from '@angular/core';

export interface Payment {
  bookingId: number;
  transactionId: string;
  amount: number;
  method: string;
  customerEmail: string;
  paidAt: string;
}

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private key = 'vv_payments';

  getAll(): Payment[] {
    try {
      return JSON.parse(localStorage.getItem(this.key) || '[]');
    } catch {
      return [];
    }
  }

  save(payment: Payment): void {
    const all = this.getAll();
    all.push(payment);
    localStorage.setItem(this.key, JSON.stringify(all));
  }

  getByBookingId(bookingId?: number): Payment | undefined {
    if (bookingId == null) return undefined;
    return this.getAll().find(p => p.bookingId === bookingId);
  }

  // Price rule: ₹50 per seat per day, minimum ₹5,000 per day
  dailyRate(capacity?: number): number {
    return Math.max(5000, (capacity ?? 100) * 50);
  }

  countDays(startDate: string, endDate: string): number {
    const start = new Date(startDate).getTime();
    const end = new Date(endDate).getTime();
    if (isNaN(start) || isNaN(end) || end < start) return 0;
    return Math.round((end - start) / 86400000) + 1;
  }

  newTransactionId(): string {
    return 'TXN' + Date.now().toString().slice(-8) + Math.floor(Math.random() * 900 + 100);
  }
}