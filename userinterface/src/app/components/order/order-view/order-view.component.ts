import { Component, OnInit, ViewChild } from '@angular/core';
import { Order } from 'src/app/models/order';
import { OrderService } from 'src/app/services/order.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { User } from 'src/app/models/user';
import { OrderPosition } from 'src/app/models/orderposition';

@Component({
  selector: 'app-order-view',
  templateUrl: './order-view.component.html',
  styleUrls: ['./order-view.component.css']
})
export class OrderViewComponent implements OnInit {
  currentUser: User;
  orders: Order[];

  constructor(
    private orderService: OrderService,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUser.subscribe(res => (this.currentUser = res));
  }

  ngOnInit() {
    this.orderService.getAllOrdersFromUser(this.currentUser.username).subscribe(res => {
      this.orders = res;
    });
  }

  calcTotal(positions: OrderPosition[]): number {
    let total = 0;
    positions.forEach(element => {
      total += element.count * element.product.price;
    });
    return total;
  }
}
