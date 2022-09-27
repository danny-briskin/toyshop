import {Injectable} from '@angular/core';
import {Payment} from './payment';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';

// service can be used in other services.
// use this decorator in every service just in case
@Injectable()

export class PaymentService {

  constructor(
    protected httpClient: HttpClient) {

  }

  private headers = new HttpHeaders({'Content-Type': 'application/json'});
  private paymentsUrl = '/api';


  getPayments(pageNumber = 0, pageSize = 17): Promise<Payment[]> {
    let payments: Payment[];

    return this.httpClient.get(this.paymentsUrl + '/payments'
      , {
        params: new HttpParams()
          .set('page', pageNumber.toString())
          .set('size', pageSize.toString())
      })
      .forEach(response => {
          payments = response['_embedded']['payments'];
          console.log(payments);
        }
      ).then(res => payments)
      .catch(this.handleError);
  }

  findPayments(
    courseId: number, filter = '', sortOrder = 'asc',
    pageNumber = 0, pageSize = 3): Observable<Payment[]> {

    const aaa = this.httpClient.get(this.paymentsUrl + '/payments'
      // , {
      // params: new HttpParams()
      //   .set('courseId', courseId.toString())
      //   .set('filter', filter)
      //   .set('sortOrder', sortOrder)
      //   .set('pageNumber', pageNumber.toString())
      //   .set('pageSize', pageSize.toString())
      // }
    ).pipe(
      map(res => {
        return res['_embedded']['payments'];
      })
    );
    return aaa;
  }

  getCustomer(id: number): Observable<any> {
    const url = `${this.paymentsUrl}/customers/${id}?projection=customerDto`;
    return this.httpClient.get(url);
  }

  getPayment(id: number): Observable<any> {
    const url = `${this.paymentsUrl}/payments/${id}?projection=paymentDto`;
    return this.httpClient.get(url)

  }

  createPayment(payment: Payment): Promise<Payment> {
    console.log("1: " + JSON.stringify(payment));
    return this.httpClient
      .post(this.paymentsUrl + '/createPayment'
        , JSON.stringify(payment), {headers: this.headers})
      .toPromise()
      .then(res => {
        console.log("3: " + JSON.stringify(res));
        return JSON.parse(JSON.stringify(res));
      })
      .catch(this.handleError);
  }

  updatePayment(payment: Payment): Promise<Payment> {
    const url = `${this.paymentsUrl}/updatePayment/${payment.paymentId}`;
    return this.httpClient
      .put(url
        , JSON.stringify(payment)
        , {headers: this.headers})
      .toPromise()
      .then(() => payment)
      .catch(this.handleError);
  }

  deletePayment(payment: Payment): Observable<any> {
    const url = `${this.paymentsUrl}/deletePayment/${payment.paymentId}`;
    return this.httpClient.delete(url, {headers: this.headers});
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}
