import { TestBed } from '@angular/core/testing';
import { SolicitudesServiceTs } from './solicitudes.service.ts';

describe('SolicitudesServiceTs', () => {
  let service: SolicitudesServiceTs;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SolicitudesServiceTs);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
