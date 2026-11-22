import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserListPage } from './user-list.page';

describe('UserListPage', () => {
  let component: UserListPage;
  let fixture: ComponentFixture<UserListPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserListPage],
    }).compileComponents();

    fixture = TestBed.createComponent(UserListPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
