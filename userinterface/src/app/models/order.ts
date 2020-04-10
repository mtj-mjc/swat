import { OrderPosition } from './orderposition';

export class Order {
  positions: OrderPosition[];
  username: string;
  customerid: string;
  storeid: string;
  date: Date;
  state: number;
}
