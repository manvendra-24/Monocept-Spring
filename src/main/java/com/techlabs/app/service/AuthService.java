package com.techlabs.app.service;

import com.techlabs.app.dto.LoginRequest;
import com.techlabs.app.dto.RegisterRequest;

public interface AuthService {
    String login(LoginRequest loginDto);

    String register(RegisterRequest registerDto, String role);
}
