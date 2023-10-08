package com.ems.emsuser.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure")
public class LoginController {

    @GetMapping("/user/{id}")
    public String userEndpoint(@PathVariable Long id) {
        // Check if the current user has the "USER" role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

        if (isUser) {
            return "This is a secure endpoint for users with ID: " + id;
        } else {
            return "Access denied for this user.";
        }
    }

    @GetMapping("/admin/{id}")
    public String adminEndpoint(@PathVariable Long id) {
        // Check if the current user has the "ADMIN" role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "This is a secure endpoint for admins with ID: " + id;
        } else {
            return "Access denied for this user.";
        }
    }
}
