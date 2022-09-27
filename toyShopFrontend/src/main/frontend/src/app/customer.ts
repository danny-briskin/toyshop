import {CustomerLinks} from './customerLinks';

export class Customer {

  id: number;
  customerName: string;
  customerActivated: string;
  customerBalance: number;
  billingAddress: string;
  customerDeactivated: string;
  _links: CustomerLinks;
}
