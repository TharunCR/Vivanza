package com.event.service;

import com.event.DTO.UserDTO;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.exception.ResourceNotFoundException;
import com.event.repository.UserRepository;
import com.event.util.UserAuthenticationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "userCache")
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Cacheable("users")
    public List<User> getAllUser() {
        doLongRunningTask();
        return userRepository.findAll();
    }

    @Cacheable(cacheNames = "user", key = "#userId", unless = "#result == null")
    public Optional<User> findById(Long userId) {
        doLongRunningTask();
        return Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId)));
    }

    @Caching(evict = {@CacheEvict(cacheNames = "users", allEntries = true),
            @CacheEvict(cacheNames = "user", key = "#userId")})
    public String updateUser(Long userId, UserDTO userRequest) throws ResourceNotFoundException, BadRequestException {
        doLongRunningTask();
        User authenticatedUser = UserAuthenticationUtil.getUserByAuthentication();
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Security Check: Ensure the authenticated user is the one being updated
        if (!authenticatedUser.getId().equals(userToUpdate.getId())) {
            throw new AccessDeniedException("You do not have permission to update this user's profile.");
        }

        // Check if the new email is already taken by another user
        if (userRepository.existsByEmail(userRequest.getEmail()) && !userToUpdate.getEmail().equals(userRequest.getEmail())) {
            throw new BadRequestException("Email '" + userRequest.getEmail() + "' is already in use. Please use a different email.");
        }

        // Update user fields from the request
        userToUpdate.setName(userRequest.getName());
        userToUpdate.setEmail(userRequest.getEmail());
        userToUpdate.setGender(userRequest.getGender());
        userToUpdate.setAddress(userRequest.getAddress());
        userToUpdate.setMobile(userRequest.getMobile());
        userToUpdate.setEventsOfInterest(userRequest.getEventsOfInterest());

        // Only update password if a new one is provided
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        userRepository.save(userToUpdate);
        return "User profile updated successfully.";
    }

    @Caching(evict = {@CacheEvict(cacheNames = "user", key = "#userId"),
            @CacheEvict(cacheNames = "users", allEntries = true)})
    public void deleteUser(Long userId) {
        doLongRunningTask();
        User authenticatedUser = UserAuthenticationUtil.getUserByAuthentication();
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Security Check: Ensure the authenticated user is the one being deleted
        if (!authenticatedUser.getId().equals(userToDelete.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this user.");
        }

        userRepository.delete(userToDelete);
        log.info("Deleted the user with Id: {}", userId);
    }

    private void doLongRunningTask() {
        try {
            Thread.sleep(3000); // Simulating a slow task
        } catch (InterruptedException e) {
            log.error("Long running task was interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}