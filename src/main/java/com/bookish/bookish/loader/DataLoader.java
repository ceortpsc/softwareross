package com.bookish.bookish.loader;

import com.bookish.bookish.model.User;
import com.bookish.bookish.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create an admin user if none exists
        if (userRepo.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", passwordEncoder.encode("adminpass"), Set.of("ROLE_ADMIN", "ROLE_MANAGER"));
            userRepo.save(admin);
            System.out.println("Created default admin user: 'admin' with password 'adminpass' (change immediately)");
        }
    }
}
