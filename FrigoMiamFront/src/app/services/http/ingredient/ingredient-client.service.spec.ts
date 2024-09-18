import { TestBed } from '@angular/core/testing';

import { IngredientClientService } from './ingredient-client.service';

describe('IngredientClientService', () => {
  let service: IngredientClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IngredientClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
