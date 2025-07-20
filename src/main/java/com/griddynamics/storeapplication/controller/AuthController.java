package com.griddynamics.storeapplication.controller;

import com.griddynamics.storeapplication.dto.request.LoginRequest;
import com.griddynamics.storeapplication.dto.request.RegisterRequest;
import com.griddynamics.storeapplication.dto.response.LoginResponse;
import com.griddynamics.storeapplication.dto.response.RegisterResponse;
import com.griddynamics.storeapplication.exception.UserNotFoundException;
import com.griddynamics.storeapplication.service.AuthService;
import com.griddynamics.storeapplication.util.CommonUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(CommonUtils.BASE_API)
public class AuthController {

  private static final String API_REGISTER   = "/register";
  private static final String API_LOGIN      = "/login";
  private static final String USER_NOT_FOUND = "Email or password is incorrect";

  @Autowired
  private AuthService authService;

  @PostMapping(API_REGISTER)
  public RegisterResponse register(@RequestBody final RegisterRequest registerRequest) {
    return authService.register(registerRequest);
  }

  @PostMapping(API_LOGIN)
  public LoginResponse login(@RequestBody final LoginRequest loginRequest) {
    return Optional
        .ofNullable(authService.login(loginRequest))
        .filter(StringUtils::isNotEmpty)
        .map(sessionId1 -> LoginResponse.builder().sessionId(sessionId1).build())
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

}
