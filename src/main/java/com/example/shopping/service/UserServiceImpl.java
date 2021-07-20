package com.example.shopping.service;

import com.example.shopping.entity.User;
import com.example.shopping.repository.RoleRepository;
import com.example.shopping.repository.UserRepository;
import com.example.shopping.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;

@Service
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
        User updateUser = findById(id);
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setEmail(user.getEmail());
        return updateUser;
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
