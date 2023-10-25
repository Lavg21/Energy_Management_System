import { TestBed } from '@angular/core/testing';

import { DeviceDialogService } from './device-dialog.service';

describe('DeviceDialogService', () => {
  let service: DeviceDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
