import { Component, OnInit } from '@angular/core';
import { WatchlistService } from '../services/watchlist.service';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrl: './watchlist.component.css'
})
export class WatchlistComponent implements OnInit {
  watchlist: any[] = [];

  displayedColumns: string[] = ['watchlistItemId', 'watchlistItemName', 'watchlistItemDescription', 'watchlistItemPrice', 'actions'];

  constructor(private watchlistService : WatchlistService) { }

  ngOnInit(): void {
    this.loadWatchlist();
  }

  loadWatchlist() {
    this.watchlistService.getWatchlist().subscribe(
      (response) => {
        console.log(response);
        this.watchlist = response;
      },
      (error) => {
        console.error('Error fetching watchlist', error);
      }
    );
  }

  removeFromWatchlist(productId: number) {
    this.watchlistService.removeFromWatchlist(productId).subscribe(
      (response: string) => {
        console.log('Remove from watchlist successfully', response);
        this.loadWatchlist();
      },
      (error) => {
        console.error('Error remove from watchlist', error);
      }
    );
  }
}
