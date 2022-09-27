import {Component, OnInit, ViewChild} from '@angular/core';
import {NavigationExtras, Router} from '@angular/router';
import {Payment} from '../payment';
import {PaymentService} from '../payment.service';
import {MatPaginator, MatTableDataSource} from '@angular/material';
import {Links} from "../links";
import {Link} from "../link";
import {Customer} from '../customer';
import {DataService} from "../data.service";
import { AlertService } from '../_alert';

const navigationExtras: NavigationExtras = {
  state: {
    projection: 'paymentDto'
  }
};

@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.css'],
  providers: [DataService]
})
export class PaymentsComponent implements OnInit {
  isNewPaymentShow = false;
  isCustomerShow = false;
  payments: Payment[];
  currentCustomer: Customer;
  selectedPayment: Payment;
  newPayment: Payment;
  displayedColumns = ['paymentId', 'paymentAmount', 'paymentDate', 'channel', 'links'];
  dataSource: MatTableDataSource<Payment>;
  options = {
        autoClose: true,
        keepAfterRouteChange: false
  };

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  private createdPayment: Payment;

  constructor(private router: Router
    , private paymentService: PaymentService
    , private dataService: DataService
    , protected alertService: AlertService) {
  }

  ngOnInit() {
    this.paymentService.getPayments(0, 500)
      .then(resources => {
        console.log(resources);
        this.dataService.createDataSource(resources,this.paginator);
        this.dataSource = this.dataService.dataSource;
      });
    this.newPayment = new Payment();
  }

  createPayment(payment: Payment): void {
    console.log(payment);
    // let createdPayment: Payment;
    this.paymentService.createPayment(payment)
      .then(payment2 => {
        console.log("4: " + JSON.stringify(payment2));

        this.createdPayment = payment2;
        console.log("5: " + JSON.stringify(this.createdPayment));
        this.createdPayment._links = new Links();
        this.createdPayment._links.customer = new Link();
        this.createdPayment._links.payment = new Link();
        this.createdPayment._links.self = new Link();

        this.createdPayment._links.customer.href = 'https://localhost:4200/api/customers/' + this.createdPayment.customerId;
        this.createdPayment._links.payment.href = 'https://localhost:4200/api/payments/' + this.createdPayment.paymentId;
        this.createdPayment._links.self.href = this.createdPayment._links.payment.href;
        console.log("6: " + JSON.stringify(this.createdPayment));
        this.dataSource.data.push(this.createdPayment);
        this.isNewPaymentShow=false;
        this.alertService.success('Payment was created successfully!', this.options)
      });
  }

  deletePayment(payment: Payment): void {
    // this.paymentService
    //   .deletePayment(payment)
    //   .then(() => {
    //     this.payments = this.payments.filter(h => h !== payment);
    //     if (this.selectedPayment === payment) {
    //       this.selectedPayment = null;
    //     }
    //   });
  }

  showInfo(payment: Payment, index: number): void {
    this.dataService.currentIndex = index;
    this.selectedPayment = payment;
    // console.log(payment);
    this.router.navigate(['/information/'
      , this.selectedPayment.paymentId], navigationExtras);
  }

  toggleNewPaymentDisplay() {
    this.isNewPaymentShow = !this.isNewPaymentShow;
  }

  toggleCustomerDisplayOn(customerId: number) {
    this.isCustomerShow = true;
    const promise = this.paymentService.getCustomer(customerId).toPromise();
    console.log(promise);
    promise.then((data) => {
      console.log(data);
      this.currentCustomer = data;
      return data;
    }).catch((error) => {
      console.log("Promise rejected with " + JSON.stringify(error));
    });
  }

  toggleCustomerDisplayOff() {
    this.isCustomerShow = false;
  }
}
