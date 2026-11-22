import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SolicitudesListPageTs } from './solicitudes-list.page.js';

describe('SolicitudesListPageTs', () => {
  let component: SolicitudesListPageTs;
  let fixture: ComponentFixture<SolicitudesListPageTs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitudesListPageTs],
    }).compileComponents();

    fixture = TestBed.createComponent(SolicitudesListPageTs);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
