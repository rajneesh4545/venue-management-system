import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Venue } from '../models/venue';

@Injectable({ providedIn: 'root' })
export class VenueService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/venues';

  getAll(): Observable<Venue[]> {
    return this.http.get<Venue[]>(this.apiUrl);
  }

  add(venue: Venue): Observable<Venue> {
    return this.http.post<Venue>(this.apiUrl, venue);
  }
}