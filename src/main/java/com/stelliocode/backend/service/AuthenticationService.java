package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.*;
import com.stelliocode.backend.entity.Technology;
import com.stelliocode.backend.entity.User;
import com.stelliocode.backend.entity.UserRole;
import com.stelliocode.backend.entity.UserTechnology;
import com.stelliocode.backend.exception.EmailAlreadyInUseException;
import com.stelliocode.backend.exception.InvalidCredentialsException;
import com.stelliocode.backend.exception.UserNotFoundException;
import com.stelliocode.backend.repository.TechnologyRepository;
import com.stelliocode.backend.repository.UserRepository;
import com.stelliocode.backend.repository.UserTechnologyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private UserTechnologyRepository userTechnologyRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthenticationService (
            UserRepository userRepository,
            TechnologyRepository technologyRepository,
            UserTechnologyRepository userTechnologyRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService
    ) {
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
        this.userTechnologyRepository = userTechnologyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPasswordHash())
                        .roles(user.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email is already in use.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        try {
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()).getRole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role specified. Valid roles are: ADMIN, DEVELOPER.");
        }
        user.setStatus("pending");
        user.setLevel(request.getLevel());
        User savedUser = userRepository.save(user);

        List<String> technologyNames = request.getTechnologies();
        for (String technologyName : technologyNames) {
            Technology technology = technologyRepository.findByName(technologyName)
                    .orElseGet(() -> {
                        Technology newTechnology = new Technology();
                        newTechnology.setName(technologyName);
                        return technologyRepository.save(newTechnology);
                    });

            UserTechnology userTechnology = new UserTechnology();
            userTechnology.setUser(savedUser);
            userTechnology.setTechnology(technology);
            userTechnologyRepository.save(userTechnology);
        }

        return new RegisterResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getStatus()
        );
    }

    public AuthenticationResponseBaseDTO authenticate(AuthenticationRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials.");
        }

        if ("pending".equals(user.getStatus())) {
            return new PendingStatusResponseDTO(
                    user.getId(),
                    user.getFullName(),
                    user.getRole(),
                    user.getStatus()
            );
        }

        String token = JwtTokenService.generateToken(user.getEmail(), user.getRole());

        return new AuthenticationResponseDTO(
                token,
                user.getId(),
                user.getFullName(),
                user.getRole(),
                user.getStatus()
        );
    }

    public RegisterResponseDTO registerAdmin(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email is already in use.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        try {
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()).getRole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role specified. Valid roles are: ADMIN, DEVELOPER.");
        }
        user.setStatus("approved");
        user.setLevel(null);

        User savedUser = userRepository.save(user);

        return new RegisterResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getStatus()
        );
    }

}
