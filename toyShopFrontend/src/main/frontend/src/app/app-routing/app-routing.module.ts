import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Location} from '@angular/common';
import {PaymentInfoComponent} from '../payment-info/payment-info.component';
import {PaymentsComponent} from '../payments/payments.component';

// list of routes with components
const routes: Routes = [
  {path: 'information/:id', component: PaymentInfoComponent},
  {path: 'payments', component: PaymentsComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  declarations: []
  , providers: [Location]
})
export class AppRoutingModule {
}
