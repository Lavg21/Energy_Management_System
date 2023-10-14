package com.ems.emsuser.controller;

import com.ems.emsuser.domain.dto.UserDTO;
import com.ems.emsuser.domain.entity.User;
import com.ems.emsuser.exception.DuplicateEmailException;
import com.ems.emsuser.exception.UserNotFoundException;
import com.ems.emsuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {

        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .admin(userDTO.isAdmin())
                .build();

        User createdUser = userService.createUser(user);

        UserDTO createdUserDTO = UserDTO.builder()
                .id(createdUser.getId())
                .name(createdUser.getName())
                .email(createdUser.getEmail())
                .password(createdUser.getPassword())
                .isAdmin(createdUser.isAdmin())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<User> users = userService.getAllUsers();
        if (users != null && !users.isEmpty()) {
            List<UserDTO> userDTOs = users.stream()
                    .map(user -> UserDTO.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .isAdmin(user.isAdmin())
                            .build())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDTOs);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id);

        if (user != null) {

            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .isAdmin(user.isAdmin())
                    .build();

            return ResponseEntity.ok(userDTO);
        } else {
//            throw new UserNotFoundException("User with ID " + id + " not found");
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
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {

        User user = userService.findUserById(id);

        if (user != null) {
            userService.deleteUser(id);
            return ResponseEntity.ok("User with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
