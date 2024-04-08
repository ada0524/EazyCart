import { Component, OnInit } from '@angular/core';
import { ProductService } from '../services/product.service';
import { WatchlistService } from '../services/watchlist.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent implements OnInit {
  isUser = this.authService.isUser();
  
  products: any[] = [];
  productDetails: any;
  watchlist: any[] = [];

  displayedColumns: string[] = ['productId', 'productName', 'actions'];

  mostFrequentlyProducts: any[] = [];
  mostRecentlyProducts: any[] = [];
  topProfitableProducts: any[] = [];
  topPopularProducts: any[] = [];


  constructor(private productService: ProductService, 
    private watchlistService : WatchlistService,
    private authService : AuthService) { }

  ngOnInit(): void {
    this.loadAllProducts();
    if (!this.isUser) {
      this.getTopProfitableProducts();
      this.getTopPopularProducts();
    }
    if (this.isUser) {
      this.getMostFrequetlyProducts();
      this.getMostRecentlyProducts();
    }
  }

  loadAllProducts() {
    this.productService.getAllProducts().subscribe(
      (response) => {
        console.log('All products:', response);
        this.products = response;
      },
      (error) => {
        console.error('Error fetching all products', error);
      }
    );
  }

  getProductDetails(productId: number) {
    this.productService.getProductDetails(productId).subscribe(
      (response) => {
        console.log('Product details:', response);
        this.productDetails = response;
      },
      (error) => {
        console.error('Error fetching product details', error);
      }
    );
  }

  loadWatchlist() {
    this.watchlistService.getWatchlist().subscribe(
      (response) => {
        console.log(response);
        this.watchlist = response;
      },
      (error) => {
        console.error('Error fetching orders', error);
      }
    );
  }

  addToWatchlist(productId: number) {
    this.watchlistService.addToWatchlist(productId).subscribe(
      (response: string) => {
        console.log('Add to watchlist successfully', response);
        this.loadWatchlist();
      },
      (error) => {
        console.error('Error canceling order', error);
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
        console.error('Error canceling order', error);
      }
    );
  }

  getMostFrequetlyProducts(): void {
    this.productService.getMostFrequetlyProducts(3)
      .subscribe(
        response => {
          this.mostFrequentlyProducts = response; 
        },
        error => {
          console.error('Error getting most frequently products:', error);
        }
      );
  }

  getMostRecentlyProducts(): void {
    this.productService.getMostRecentlyProducts(3)
      .subscribe(
        response => {
          this.mostRecentlyProducts = response; 
        },
        error => {
          console.error('Error getting most recently products:', error);
        }
      );
  }

  getTopProfitableProducts(): void {
    this.productService.getTopProfitableProducts(3)
      .subscribe(
        response => {
          this.mostRecentlyProducts = response; 
        },
        error => {
          console.error('Error getting most recently products:', error);
        }
      );
  }

  getTopPopularProducts(): void {
    this.productService.getTopPopularProducts(3)
      .subscribe(
        response => {
          this.topPopularProducts = response; 
        },
        error => {
          console.error('Error getting top popular products:', error);
        }
      );
  }

  addProduct(newProductForm: any): void {
    const newProduct = newProductForm.value;

    this.productService.addProduct(newProduct)
      .subscribe(
        response => {
          console.log(response);
          this.loadAllProducts();
        },
        error => {
          console.error('Error adding product:', error);
        }
      );
  }

}
