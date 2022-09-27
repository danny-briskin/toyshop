import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule} from '@angular/forms';
import {PaymentService} from './payment.service';
import {AppRoutingModule} from './app-routing/app-routing.module';
import {PaymentInfoComponent} from './payment-info/payment-info.component';
import {PaymentsComponent} from './payments/payments.component';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
  MatButtonModule,
  MatCheckboxModule,
  MatDatepickerModule,
  MatGridListModule,
  MatListModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressSpinnerModule,
  MatSnackBarModule,
  MatSortModule,
  MatTableModule
} from '@angular/material';
import {MAT_MOMENT_DATE_FORMATS, MatMomentDateModule} from '@angular/material-moment-adapter';
import {HttpClientModule} from '@angular/common/http';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatCardModule} from '@angular/material/card';
import {MomentUtcDateAdapter} from './MomentUtcDateAdapter';
import {AlertModule} from './_alert';

@NgModule({
  declarations: [
    AppComponent,
    PaymentsComponent,
    PaymentInfoComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    MatButtonModule,
    MatCheckboxModule,
    MatCardModule,
    MatListModule,
    MatSelectModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatMomentDateModule,
    MatGridListModule,
    MatSnackBarModule,
    AppRoutingModule,
    AlertModule,
    BrowserAnimationsModule
  ],
  providers: [
    PaymentService,
    {provide: MAT_DATE_LOCALE, useValue: 'en-CA'},
    {provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS},
    {provide: DateAdapter, useClass: MomentUtcDateAdapter}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
