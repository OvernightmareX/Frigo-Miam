import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeUserComponent } from './recipe-user.component';

describe('RecipeUserComponent', () => {
  let component: RecipeUserComponent;
  let fixture: ComponentFixture<RecipeUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeUserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
