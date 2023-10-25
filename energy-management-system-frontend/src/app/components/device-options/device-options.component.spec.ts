import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceOptionsComponent } from './device-options.component';

describe('DeviceOptionsComponent', () => {
  let component: DeviceOptionsComponent;
  let fixture: ComponentFixture<DeviceOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeviceOptionsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeviceOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});