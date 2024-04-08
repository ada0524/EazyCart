package org.example.springsecurityauth.service;

import org.example.springsecurityauth.dao.PermissionDao;
import org.example.springsecurityauth.dao.UserDao;
import org.example.springsecurityauth.domain.response.RegisterResponse;
import org.example.springsecurityauth.entity.Permission;
import org.example.springsecurityauth.entity.User;
import org.example.springsecurityauth.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RegisterService {

    private UserDao userDao;
    private PermissionDao permissionDao;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserDao userDao, PermissionDao permissionDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.permissionDao = permissionDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String email, String password) {
        if (userDao.getUserByUsername(username).isPresent()
            || userDao.getUserByEmail(email).isPresent()) {
            return null;
        }

        // Perform user registration logic
        User user = userDao.createUser(username, email, passwordEncoder.encode(password));
        String[] permissionValues = new String[]{"user_read", "user_write", "user_update", "user_delete"};
        userDao.saveUser(user);
        Set<Permission> permissions = permissionDao.createUserDefaultPermission(user, permissionValues);
        for (Permission permission : permissions) {
            permissionDao.savePermission(permission);
        }

        return user;
    }
}

