package com.ems.emsuser.service;

import com.ems.emsuser.domain.dto.UserAvailableDTO;
import com.ems.emsuser.domain.dto.UserDTO;
import com.ems.emsuser.domain.entity.User;
import com.ems.emsuser.exception.ClientException;
import com.ems.emsuser.exception.ClientServerException;
import com.ems.emsuser.exception.DuplicateEmailException;
import com.ems.emsuser.exception.UserNotFoundException;
import com.ems.emsuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(13);

    @Value("${device.container.name}")
    private String deviceContainerName;

    public User convertToEntity(UserDTO userDTO) {

        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .admin(userDTO.isAdmin())
                .build();
    }

    public UserDTO convertToDTO(User user) {

        return  UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .admin(user.isAdmin())
                .build();
    }

    public List<UserDTO> getUserDTOS(List<User> users) {
        return users.stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .admin(user.isAdmin())
                        .build())
                .collect(Collectors.toList());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO createUser(UserDTO userDTO) {

        User user = convertToEntity(userDTO);

        if (StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getPassword()))
            throw new IllegalArgumentException("Invalid data for user!");

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new DuplicateEmailException("Email address is already in use!");
        }

        user.setPassword(encodePassword(user.getPassword()));

        User savedUser = userRepository.save(user);

        insertIntoAvailableUsers(savedUser.getId());

        return convertToDTO(savedUser);
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
        existingUser.setAdmin(userDTO.isAdmin());

        User updatedUser = userRepository.save(existingUser);

        return convertToDTO(updatedUser);
    }

    public void deleteUser(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found!"));

        updateAvailableUsersAfterDelete(id);

        userRepository.delete(user);
    }

    private void insertIntoAvailableUsers(Integer userID) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        UserAvailableDTO userAvailableDTO = new UserAvailableDTO(userID);
        HttpEntity<UserAvailableDTO> httpEntity = new HttpEntity<>(userAvailableDTO, httpHeaders);

        try {
            String url = "http://" + deviceContainerName + ":8081/userAvailable";
            ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new ClientException("The user could not be inserted!");
            }
        } catch (HttpServerErrorException exception) {
            throw new ClientServerException("An unexpected error occurred when trying to insert an user!");
        }
    }

    private void updateAvailableUsersAfterDelete(Integer userID) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            String url = "http://" + deviceContainerName + ":8081/userAvailable/";
            ResponseEntity<String> responseEntity = restTemplate.exchange(url + userID, HttpMethod.DELETE, httpEntity, String.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new ClientException("The user could not be deleted!");
            }
        } catch (HttpServerErrorException exception) {
            throw new ClientServerException("An unexpected error occurred when trying to insert an user!");
        }
    }

}
