import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientTabFrigoComponent } from './ingredient-tab-frigo.component';

describe('IngredientTabFrigoComponent', () => {
  let component: IngredientTabFrigoComponent;
  let fixture: ComponentFixture<IngredientTabFrigoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngredientTabFrigoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IngredientTabFrigoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
