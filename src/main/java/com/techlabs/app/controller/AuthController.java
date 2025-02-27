package com.techlabs.app.controller;

import com.techlabs.app.dto.AuthResponse;
import com.techlabs.app.dto.LoginRequest;
import com.techlabs.app.dto.RegisterRequest;
import com.techlabs.app.service.AuthService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Login to admin or customer
    @PostMapping(value = "/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginDto){
		logger.info("A user trying to login");
        String token = authService.login(loginDto);
        System.out.println(loginDto);
        AuthResponse jwtAuthResponse = new AuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    //Register a admin and customer
    @PostMapping(value = "/auth/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerDto, @RequestParam(name="role") String role){
    	logger.info("A new user trying to register");
        String response = authService.register(registerDto, role);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
