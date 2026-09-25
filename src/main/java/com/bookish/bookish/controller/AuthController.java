package com.bookish.bookish.controller;

import com.bookish.bookish.model.User;
import com.bookish.bookish.repository.UserRepository;
import com.bookish.bookish.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body(Map.of("error","username and password required"));
        if (userRepo.findByUsername(username).isPresent()) return ResponseEntity.badRequest().body(Map.of("error","user exists"));
        User u = new User(username, passwordEncoder.encode(password), Set.of("ROLE_VIEWER"));
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message","user created"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body(Map.of("error","username and password required"));

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error","invalid credentials"));
        }

        var user = userRepo.findByUsername(username).orElseThrow();
        var token = jwtUtil.generateToken(username, List.copyOf(user.getRoles()));
        return ResponseEntity.ok(Map.of("token", token, "roles", user.getRoles()));
    }
}
