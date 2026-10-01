import { Venue } from './venue';

export interface Booking {
  id?: number;
  customerName: string;
  customerEmail: string;
  startDate: string;
  endDate: string;
  status?: string;
  venue: Partial<Venue>;
}