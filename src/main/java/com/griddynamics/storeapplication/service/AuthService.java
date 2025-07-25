package com.griddynamics.storeapplication.service;

import com.griddynamics.storeapplication.dto.request.LoginRequest;
import com.griddynamics.storeapplication.dto.request.RegisterRequest;
import com.griddynamics.storeapplication.dto.response.RegisterResponse;
import com.griddynamics.storeapplication.entity.User;
import com.griddynamics.storeapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class AuthService {

  private static final String USER_REGISTERED     = "User registered successfully";
  private static final String USER_ALREADY_EXISTS = "User with this email address already exists";

  @Autowired
  private UserRepository        userRepository;
  @Autowired
  private PasswordEncoder       passwordEncoder;
  @Autowired
  private SessionManagerService sessionManagerService;

  public RegisterResponse register(@RequestBody final RegisterRequest registerRequest) {
    final String email = registerRequest.getEmail();
    final String password = registerRequest.getPassword();

    Optional<User> user = userRepository.findByEmail(email);
    if (user.isPresent()) {
      return RegisterResponse.builder().status(HttpStatus.CONFLICT.value()).message(USER_ALREADY_EXISTS).build();
    }

    String encodedPassword = passwordEncoder.encode(password);
    userRepository.save(new User(email, encodedPassword));

    return RegisterResponse.builder().status(HttpStatus.OK.value()).message(USER_REGISTERED).build();
  }

  public String login(final LoginRequest loginRequest) {
    return userRepository
        .findByEmail(loginRequest.getEmail())
        .filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getEncodedPassword()))
        .map(user -> sessionManagerService.createSession(user.getId()))
        .orElse(null);
  }

}

