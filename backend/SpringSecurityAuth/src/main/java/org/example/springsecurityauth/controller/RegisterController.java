package org.example.springsecurityauth.controller;

import org.example.springsecurityauth.dao.UserDao;
import org.example.springsecurityauth.domain.request.RegisterRequest;
import org.example.springsecurityauth.domain.response.RegisterResponse;
import org.example.springsecurityauth.entity.Permission;
import org.example.springsecurityauth.entity.User;
import org.example.springsecurityauth.exception.UserAlreadyExistsException;
import org.example.springsecurityauth.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
public class RegisterController {

    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/signup")
    public RegisterResponse signup(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return RegisterResponse.builder()
                    .message("Registration failed. Please check your input.")
                    .build();
        }

        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();

        User user = registerService.registerUser(username, email, password);

        if (user == null) {
            throw new UserAlreadyExistsException("User already exists, please log in.");
        }
        else {
            return RegisterResponse.builder()
                    .message("User registered successfully.")
                    .build();
        }
    }
}
