package org.example.springsecurityauth.controller;

import org.example.springsecurityauth.domain.request.LoginRequest;
import org.example.springsecurityauth.domain.response.LoginResponse;
import org.example.springsecurityauth.exception.InvalidCredentialsException;
import org.example.springsecurityauth.security.AuthUserDetail;
import org.example.springsecurityauth.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Autowired
    public LoginController(
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;

    }

    // User trying to log in with username and password
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){

        Authentication authentication;

        // Try to authenticate the user using the username and password
        try{
          authentication = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }
        catch (AuthenticationException e){
            throw new InvalidCredentialsException("Provided credential is invalid.");
        }

        // Successfully authenticated user will be stored in the authUserDetail object
        AuthUserDetail authUserDetail = (AuthUserDetail) authentication.getPrincipal(); //getPrincipal() returns the user object

        // A token wil be created using the username/email/userId and permission
        String token = jwtProvider.createToken(authUserDetail);

        // Returns the token as a response to the frontend/postman
        return LoginResponse.builder()
                .message("Welcome " + authUserDetail.getUsername())
                .token(token)
                .build();

    }
}
// (LoginController: call AuthenticationManager in securityConfig.
// AuthenticationManager: automatically loads the DaoAuthenticationProvider.
// DaoAuthenticationProvider: calls the userDetailsService.
    // UserDetailsService: retrieve object from UserDao by username -> encapsulate as userDetail.
    // DaoAuthenticationProvider then compare password in UserDetail and database
// AuthenticationManager: authenticated UserDetail is then automatically stored in the authentication
// Authentication: .getPrincipal() to get UserDetails, and encrypt it using jwtProvider
// return jwt to frontend)
