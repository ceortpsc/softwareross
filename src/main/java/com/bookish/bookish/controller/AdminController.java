package com.bookish.bookish.controller;

import com.bookish.bookish.model.User;
import com.bookish.bookish.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public List<User> users() { return userRepo.findAll(); }

    @PostMapping("/users/{id}/roles")
    public ResponseEntity<?> updateRoles(@PathVariable Long id, @RequestBody Map<String,Object> body) {
        Optional<User> ou = userRepo.findById(id);
        if (ou.isEmpty()) return ResponseEntity.notFound().build();
        User u = ou.get();
        Object r = body.get("roles");
        if (r instanceof List) {
            //noinspection unchecked
            u.setRoles(SetFromObj((List<Object>) r));
            userRepo.save(u);
            return ResponseEntity.ok(Map.of("message","roles updated"));
        }
        return ResponseEntity.badRequest().body(Map.of("error","roles must be an array"));
    }

    @PostMapping("/users/{id}/password")
    public ResponseEntity<?> setPassword(@PathVariable Long id, @RequestBody Map<String,String> body) {
        String pwd = body.get("password");
        if (pwd==null) return ResponseEntity.badRequest().body(Map.of("error","password required"));
        Optional<User> ou = userRepo.findById(id);
        if (ou.isEmpty()) return ResponseEntity.notFound().build();
        User u = ou.get();
        u.setPassword(passwordEncoder.encode(pwd));
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message","password updated"));
    }

    private java.util.Set<String> SetFromObj(List<Object> arr){
        java.util.Set<String> s = new java.util.HashSet<>();
        for(Object o: arr) s.add(String.valueOf(o));
        return s;
    }
}
