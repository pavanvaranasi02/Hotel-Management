package com.pavan.security_service.service;

import com.pavan.security_service.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    String saveUser(User user);
    String generateToken(String username);
    void validateToken(String token);
}
