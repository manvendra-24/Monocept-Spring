package com.techlabs.app.service;

import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.dto.LoginRequest;
import com.techlabs.app.dto.RegisterRequest;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginRequest loginDto) {
    	String usernameOrEmail = loginDto.getUsernameOrEmail();
    	String password = loginDto.getPassword();
    	UsernamePasswordAuthenticationToken temp = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);
        Authentication authentication = authenticationManager.authenticate(temp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String register(RegisterRequest registerDto, String role) {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new ApiException("Username is already exists!.");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new ApiException("Email is already exists!.");
        }
        Optional<Role> oUserRole = roleRepository.findByName(role);
        if(oUserRole.isEmpty()) {
        	throw new ResourceNotFoundException("Role does not exists");
        }
        Role userRole = oUserRole.get();
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(userRole);
        
        if(userRole.getName().equals("CUSTOMER")) {
        	Customer customer = new Customer();
            customer.setFirstname(registerDto.getFirstname());
            customer.setLastname(registerDto.getLastname());
            customer.setTotalbalance(0);
            customerRepository.save(customer);
            user.setCustomer(customer);
        }
        
        userRepository.save(user);
        return "User registered successfully!.";
    }
}
