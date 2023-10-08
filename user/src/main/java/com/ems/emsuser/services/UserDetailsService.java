package com.ems.emsuser.services;

import com.ems.emsuser.domain.UserPrincipal;
import com.ems.emsuser.domain.entity.User;
import com.ems.emsuser.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("No user with specified email address was found!");
        }

        return new UserPrincipal(user);
    }

    public User getCurrentlyLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserPrincipal) {
            Integer id = ((UserPrincipal) principal).getId();
            return userRepository.findById(id).orElse(null);
        }
        throw new EntityNotFoundException("Could not get the currently logged user!");
    }
}
