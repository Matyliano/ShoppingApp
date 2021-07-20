package com.example.shopping.security;

import com.example.shopping.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final UserServiceImpl userService;

    public boolean hasAccessToUser(Long id) {
        try {
            return userService.getCurrentUser().getId().equals(id);
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}
