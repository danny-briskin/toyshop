import {Injectable} from '@angular/core';
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {Payment} from "./payment";

@Injectable()

export class DataService {
  private _dataSource: MatTableDataSource<Payment>;
  private _currentIndex: number;

  createDataSource(resources: Payment[],paginator: MatPaginator) {
     console.log(resources);
    this._dataSource = new MatTableDataSource(resources);
    this._dataSource.paginator = paginator;
  }

  get dataSource(): MatTableDataSource<Payment> {
    return this._dataSource;
  }

  get currentIndex(): number {
    return this._currentIndex;
  }

  set currentIndex(value: number) {
    this._currentIndex = value;
  }
}
