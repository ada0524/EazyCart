package org.example.springsecurityauth.service;

import org.example.springsecurityauth.dao.UserDao;
import org.example.springsecurityauth.entity.Permission;
import org.example.springsecurityauth.entity.User;
import org.example.springsecurityauth.security.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    // Map permission to GrantedAuthority and generate UserDetails object.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userDao.getUserByUsername(username);

        if (!userOptional.isPresent()){
            throw new UsernameNotFoundException("Username does not exist");
        }

        User user = userOptional.get();

        return AuthUserDetail.builder() // spring security's userDetail
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthoritiesFromUser(user))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    private List<GrantedAuthority> getAuthoritiesFromUser(User user){
        List<GrantedAuthority> userAuthorities = new ArrayList<>();

        for (Permission permission :  user.getPermissions()){
            userAuthorities.add(new SimpleGrantedAuthority(permission.getValue()));
        }

        return userAuthorities;
    }
}
