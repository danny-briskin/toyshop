import {Payment} from '../payment';
import {PaymentService} from '../payment.service';
import {catchError, finalize} from 'rxjs/operators';
import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable} from 'rxjs';
import {of} from 'rxjs/internal/observable/of';

export class PaymentsDataSource implements DataSource<Payment> {

  private lessonsSubject = new BehaviorSubject<Payment[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private coursesService: PaymentService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<Payment[]> {
    return this.lessonsSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.lessonsSubject.complete();
    this.loadingSubject.complete();
  }

  loadLessons(courseId: number, filter = '',
              sortDirection = 'asc', pageIndex = 0, pageSize = 3) {

    this.loadingSubject.next(true);

    this.coursesService.findPayments(courseId, filter, sortDirection,
      pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(lessons => this.lessonsSubject.next(lessons));
  console.log(this.lessonsSubject);

  }
}
