package com.security.controller;

import com.security.model.User;
import com.security.repository.UserRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/public")
@CrossOrigin
class PublicRestApiController {
    private UserRepository userRepository;

    public PublicRestApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("test")
    public String test1() {
        return "API test";
    }

    @GetMapping("management/reports")
    public String reports() {
        return "some reports data";
    }

    @GetMapping("users")
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
