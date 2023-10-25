import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MappingOptionsComponent } from './mapping-options.component';

describe('MappingOptionsComponent', () => {
  let component: MappingOptionsComponent;
  let fixture: ComponentFixture<MappingOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MappingOptionsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MappingOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
