package com.ems.emsdevice.controller;

import com.ems.emsdevice.domain.dto.UserAvailableDTO;
import com.ems.emsdevice.exception.UserAvailableNotFoundException;
import com.ems.emsdevice.service.UserAvailableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/userAvailable")
public class UserAvailableController {

    private final UserAvailableService userAvailableService;

    @PostMapping
    public ResponseEntity<?> addUserAvailable(@RequestBody UserAvailableDTO userAvailableDTO) {
        try {
            userAvailableService.createUserAvailable(userAvailableDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(userAvailableDTO);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserAvailable(@PathVariable Integer id) {
        try {
            userAvailableService.deleteUserAvailable(id);

            return ResponseEntity.ok("User available with ID " + id + " has been deleted");
        } catch (UserAvailableNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
