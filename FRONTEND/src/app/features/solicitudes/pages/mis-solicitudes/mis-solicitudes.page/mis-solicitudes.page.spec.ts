import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MisSolicitudesPage } from './mis-solicitudes.page';

describe('MisSolicitudesPage', () => {
  let component: MisSolicitudesPage;
  let fixture: ComponentFixture<MisSolicitudesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisSolicitudesPage],
    }).compileComponents();

    fixture = TestBed.createComponent(MisSolicitudesPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
