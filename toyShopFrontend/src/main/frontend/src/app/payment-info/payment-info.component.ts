import {Component, OnInit} from '@angular/core';
import {Payment} from '../payment';
import {PaymentService} from '../payment.service';
import {ActivatedRoute, Params} from '@angular/router';
import {Location} from '@angular/common';
import {switchMap} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Response} from '../response';
import {DataService} from "../data.service";

@Component({
  selector: 'app-payment-info',
  templateUrl: './payment-info.component.html',
  styleUrls: ['./payment-info.component.css'],
  providers: [DataService]
})
export class PaymentInfoComponent implements OnInit {
  payment: Payment;

  constructor(private paymentService: PaymentService,
              private route: ActivatedRoute,
              private location: Location,
              private _snackBar: MatSnackBar,
              private dataService: DataService) {
  }

  ngOnInit(): void {
    this.route.params.pipe(switchMap(
      (params: Params) => this.paymentService.getPayment(+params['id'])
    ))
      .subscribe(payment => this.payment = payment);
    console.log(this.payment);
  }

  updatePayment(): void {
    this.paymentService.updatePayment(this.payment);
    this.goBack();
  }

  deletePayment(): void {
    this.paymentService.deletePayment(this.payment)
      .subscribe(
        data => {
          let str: Response = <Response>data;
          this._snackBar.open('Payment ID ' + str.paymentId
            , 'was ' + str.status, {
              duration: 2000,
            });
          return data;
        }
      );
    this.goBack();
  }

  goBack(): void {
    this.location.back();
  }
}
