package com.event.controller;

import com.event.DTO.UserDTO;
import com.event.entity.User;
import com.event.exception.ResourceNotFoundException;
import com.event.repository.UserRepository;
import com.event.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userId) {
        try {
            log.info("Fetching user with ID {}", userId);
            // Assuming the service method is updated to not require locale
            Optional<User> optionalUser = userService.findById(userId);
            User user = optionalUser.orElse(new User());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            log.error("User with ID {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userId, @Valid @RequestBody UserDTO userDetails) {
        try {
            // Assuming the service method is updated to not require locale
            String updatedUser = userService.updateUser(userId, userDetails);
            log.info("Updated user with ID {}", userId);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            log.error("User with ID {} not found for update", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        } catch (BadRequestException ex) {
            log.error("User email '{}' already exists with another user", userDetails.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email '" + userDetails.getEmail() + "' is already in use. Please use a different email.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        log.info("Deleting user with ID {}", userId);
        try {
             // Assuming the service method is updated to not require locale.
            userService.deleteUser(userId);
            log.info("Deleted user with ID {}", userId);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully with ID: " + userId);
        } catch (ResourceNotFoundException ex) {
            log.error("User with ID {} not found for deletion", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found or you do not have permission to delete it. ID: " + userId);
        }
    }
}