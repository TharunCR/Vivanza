package com.event.service;

import com.event.DTO.JwtAuthenticationResponse;
import com.event.DTO.SigninRequest;
import com.event.DTO.UserDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    /**
     * Registers a new user.
     * @param userDTO The user data for registration.
     * @return A confirmation message.
     * @throws BadRequestException if the user data is invalid (e.g., email already exists).
     */
    String registration(UserDTO userDTO) throws BadRequestException;

    /**
     * Authenticates a user and provides a JWT token.
     * @param request The sign-in credentials.
     * @return A response containing the JWT token.
     */
    JwtAuthenticationResponse login(SigninRequest request);
}