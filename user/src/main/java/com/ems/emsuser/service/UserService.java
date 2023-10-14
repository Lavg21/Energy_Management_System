package com.ems.emsuser.service;

import com.ems.emsuser.domain.dto.UserDTO;
import com.ems.emsuser.domain.entity.User;
import com.ems.emsuser.exception.DuplicateEmailException;
import com.ems.emsuser.exception.UserNotFoundException;
import com.ems.emsuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public User createUser(User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new DuplicateEmailException("Email address is already in use.");
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDTO updateUser(Integer id, UserDTO userDTO) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        String newEmail = userDTO.getEmail();
        if (!existingUser.getEmail().equals(newEmail) && userRepository.findByEmail(newEmail).isPresent()) {
            throw new DuplicateEmailException("Email is already in use by another user");
        }

        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(userDTO.getPassword());
        existingUser.setAdmin(userDTO.isAdmin());

        User updatedUser = userRepository.save(existingUser);

        return UserDTO.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .password(updatedUser.getPassword())
                .isAdmin(updatedUser.isAdmin())
                .build();
    }

    public void deleteUser(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        userRepository.delete(user);
    }

}
