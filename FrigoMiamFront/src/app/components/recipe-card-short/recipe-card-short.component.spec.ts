import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeCardShortComponent } from './recipe-card-short.component';

describe('RecipeCardShortComponent', () => {
  let component: RecipeCardShortComponent;
  let fixture: ComponentFixture<RecipeCardShortComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeCardShortComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeCardShortComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
