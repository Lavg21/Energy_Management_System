package com.ems.emsuser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String testAdminMethod() {
        return "You are logged in as ADMIN!";
    }

    @GetMapping("/client")
    public String testClientMethod() {
        return "You are logged in as CLIENT!";
    }
}
