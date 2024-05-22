package com.ems.emsdevice.service;

import com.ems.emsdevice.domain.dto.UserAvailableDTO;
import com.ems.emsdevice.domain.entity.UserAvailable;
import com.ems.emsdevice.exception.UserAvailableNotFoundException;
import com.ems.emsdevice.repository.UserAvailableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAvailableService {

    private final UserAvailableRepository userAvailableRepository;

    public UserAvailableDTO createUserAvailable(UserAvailableDTO userAvailableDTO) {
        UserAvailable userAvailable = new UserAvailable(userAvailableDTO.getId());
        userAvailableRepository.save(userAvailable);

        return userAvailableDTO;
    }

    public void deleteUserAvailable(Integer id) {
        UserAvailable userAvailable = userAvailableRepository
                .findById(id)
                .orElseThrow(() -> new UserAvailableNotFoundException("User available with ID " + id + " not found"));

        userAvailableRepository.delete(userAvailable);
    }
}
