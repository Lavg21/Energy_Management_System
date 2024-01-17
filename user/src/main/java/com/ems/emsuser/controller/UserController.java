package com.ems.emsuser.controller;

import com.ems.emsuser.domain.dto.LoginRequestDTO;
import com.ems.emsuser.domain.dto.LoginResponseDTO;
import com.ems.emsuser.domain.dto.UserDTO;
import com.ems.emsuser.domain.entity.User;
import com.ems.emsuser.exception.DuplicateEmailException;
import com.ems.emsuser.exception.UserNotFoundException;
import com.ems.emsuser.service.JWTService;
import com.ems.emsuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    private final JWTService jwtService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO createdUserDTO = userService.createUser(userDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdUserDTO);
        } catch (IllegalArgumentException | DuplicateEmailException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            return ResponseEntity.ok(userService.login(loginRequestDTO));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users != null && !users.isEmpty()) {
            List<UserDTO> userDTOs = userService.getUserDTOS(users);

            return ResponseEntity.ok(userDTOs);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/token")
    public ResponseEntity<UserDTO> getUserByToken(@RequestHeader("Authorization") String token) {

        User user = userService.findUserById(jwtService.getIdFromToken(token.substring(7)));

        if (user != null) {

            UserDTO userDTO = userService.convertToDTO(user);

            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id);

        if (user != null) {

            UserDTO userDTO = userService.convertToDTO(user);

            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUserDTO = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

        try {
            userService.deleteUser(id);

            return ResponseEntity.ok("User with ID " + id + " has been deleted");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
