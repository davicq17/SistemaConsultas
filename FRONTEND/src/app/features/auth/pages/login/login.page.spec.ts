import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginPageTs } from './login.page.js';

describe('LoginPageTs', () => {
  let component: LoginPageTs;
  let fixture: ComponentFixture<LoginPageTs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginPageTs],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginPageTs);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
