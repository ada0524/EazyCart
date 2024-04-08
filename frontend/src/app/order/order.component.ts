import { Component, OnInit } from '@angular/core';
import { OrderService } from '../services/order.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent implements OnInit {
  isUser = this.authService.isUser();
  orders: any[] = [];
  orderDetails: any;

  displayedColumns: string[] = ['orderId', 'datePlaced', 'orderStatus', 'actions'];

  constructor(private orderService: OrderService,
    private authService: AuthService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.orderService.getAllOrders().subscribe(
      (response) => {
        console.log(response);
        this.orders = response;
      },
      (error) => {
        console.error('Error fetching orders', error);
      }
    );
  }

  getOrderDetails(orderId: number) {
    this.orderService.getOrderDetails(orderId).subscribe(
      (orderDetails) => {
        console.log('Order details:', orderDetails);
        this.orderDetails = orderDetails; 
      },
      (error) => {
        console.error('Error fetching order details', error);
      }
    );
  }

  cancelOrder(orderId: number) {
    this.orderService.cancelOrder(orderId).subscribe(
      (response: string) => {
        console.log('Order canceled successfully', response);
        this.loadOrders();
      },
      (error) => {
        console.error('Error canceling order', error);
      }
    );
  }

  completeOrder(orderId: number) {
    this.orderService.completeOrder(orderId).subscribe(
      (response: string) => {
        console.log('Order completed successfully', response);
        this.loadOrders();
      },
      (error) => {
        console.error('Error completing order', error);
      }
    );
  }

}
