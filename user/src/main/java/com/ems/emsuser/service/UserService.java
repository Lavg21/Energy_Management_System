package com.ems.emsuser.service;

import com.ems.emsuser.domain.dto.UserDTO;
import com.ems.emsuser.domain.entity.User;
import com.ems.emsuser.exception.DuplicateEmailException;
import com.ems.emsuser.exception.UserNotFoundException;
import com.ems.emsuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(13);

    public User createUser(User user) {

        if (StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getPassword()))
            throw new IllegalArgumentException("Invalid data for user!");

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new DuplicateEmailException("Email address is already in use!");
        }

        user.setPassword(generateAndEncodePassword());

        return userRepository.save(user);
    }

    private String generateAndEncodePassword() {
        String password = RandomStringUtils.random(10, true, true);

        return passwordEncoder.encode(password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDTO updateUser(Integer id, UserDTO userDTO) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found!"));

        String newEmail = userDTO.getEmail();
        if (!existingUser.getEmail().equals(newEmail) && userRepository.findByEmail(newEmail).isPresent()) {
            throw new DuplicateEmailException("Email is already in use by another user!");
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
                .admin(updatedUser.isAdmin())
                .build();
    }

    public void deleteUser(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found!"));

        userRepository.delete(user);
    }

}
