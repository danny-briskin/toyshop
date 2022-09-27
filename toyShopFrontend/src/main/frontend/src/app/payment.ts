import {Links} from './links';

export class Payment {

  paymentId: number;
  paymentAmount: number;
  channel: string;
  paymentDate: string;
  customerId: number;
  _links: Links;
}
