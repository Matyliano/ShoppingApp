package com.example.shopping.service;

import com.example.shopping.entity.User;
import com.example.shopping.repository.RoleRepository;
import com.example.shopping.repository.UserRepository;
import com.example.shopping.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;

public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    public User save(User user) {
        roleRepository.findByName("ROLE_USER").ifPresent(role -> user.setRoles(Collections.singletonList(role)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " doesn't exist"));
    }


    @Transactional
    public User update(User user, Long id) {
        User userDb = findById(id);
        userDb.setFirstName(user.getFirstName());
        userDb.setLastName(user.getLastName());
        userDb.setEmail(user.getEmail());

        return userDb;
    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public Page<User> getPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    public User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not logged"));
    }
}
