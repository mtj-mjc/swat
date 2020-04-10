import { Component, OnInit } from '@angular/core';
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource } from '@angular/material';
import { OrderPosition } from 'src/app/models/orderposition';
import { Customer } from 'src/app/models/customer';
import { Store } from 'src/app/models/store';
import { Order } from 'src/app/models/order';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { ProductService } from 'src/app/services/product.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-order-create',
  templateUrl: './order-create.component.html',
  styleUrls: ['./order-create.component.css']
})
export class OrderCreateComponent implements OnInit {
  storeid: string;
  customerid: string;
  currentUser: User;
  positions: OrderPosition[];
  displayedColumns: string[] = ['select', 'name', 'price', 'quantity'];
  dataSource = new MatTableDataSource<OrderPosition>(this.positions);
  selection = new SelectionModel<OrderPosition>(true, []);
  orderPositions: OrderPosition[] = [];
  total = 0;

  customers: Customer[] = [
    { id: '1', name: 'Kevin Bauer' },
    { id: '2', name: 'Anna Voss' },
    { id: '3', name: 'Andrin Knutwiler' }
  ];
  stores: Store[];

  constructor(
    private userService: UserService,
    private productService: ProductService,
    private orderService: OrderService,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUser.subscribe(res => (this.currentUser = res));
  }

  ngOnInit() {
    this.userService.getAllStores().subscribe(res => {
      this.stores = res;
    });
    this.productService.getAllProducts().subscribe(res => {
      this.positions = res.map(p => {
        return { product: p, count: 1 } as OrderPosition;
      });
    });
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  masterToggle() {
    if (this.isAllSelected()) {
      this.selection.clear();
      this.orderPositions = [];
      this.updateTotal();
    } else {
      this.selection.clear();
      this.orderPositions = [];
      this.dataSource.data.forEach(row => {
        this.selection.select(row);
        this.addProduct(row);
      });
    }
    console.log(this.orderPositions);
  }

  checkboxLabel(row?: OrderPosition): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.product.id + 1}`;
  }

  addProduct(position: OrderPosition) {
    this.orderPositions.push(position);
    console.log(this.orderPositions);
    this.updateTotal();
  }

  removeProduct(position: OrderPosition) {
    for (let i = 0; i < this.orderPositions.length; i++) {
      if (this.orderPositions[i].product.id === position.product.id) {
        this.orderPositions.splice(i, 1);
      }
    }
    this.updateTotal();
    console.log(this.orderPositions);
  }

  updateQuantity(position: OrderPosition) {
    const itemIndex = this.orderPositions.findIndex(
      item => item.product.id === position.product.id
    );
    if (itemIndex >= 0) {
      this.orderPositions[itemIndex].count = position.count;
    }
    this.updateTotal();
  }

  updateTotal() {
    this.total = 0;
    this.orderPositions.forEach(row => {
      this.total += row.product.price * row.count;
    });
  }

  sendOrder() {
    const order = new Order();
    order.positions = this.orderPositions;
    order.username = this.currentUser.username;
    order.customerid = this.customerid;
    order.storeid = this.storeid;
    order.date = new Date();
    console.log(order);
    this.orderService.createOrder(order).subscribe(res => {
      console.log(res);
    });
  }
}
