package com.pavan.security_service.service;

import com.pavan.security_service.entity.User;
import com.pavan.security_service.exception.UserAlreadyExists;
import com.pavan.security_service.repository.UserRepository;
import com.pavan.security_service.utils.Constant;
import com.pavan.security_service.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String saveUser(User user){
        Optional<User> optionalUser = repository.findByUsername(user.getUsername());
        if(optionalUser.isPresent()) {
            throw new UserAlreadyExists("User Already Exists, try with unique usernames.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return Constant.USER_SAVED;
    }

    @Override
    public String generateToken(String username){
        return jwtService.generateToken(username);
    }

    @Override
    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
