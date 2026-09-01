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
        // Allow rotating admin password via ADMIN_PASSWORD env var. If provided, update existing admin or create one.
        String adminPwd = System.getenv("ADMIN_PASSWORD");
        if (adminPwd != null && !adminPwd.isBlank()) {
            var maybe = userRepo.findByUsername("admin");
            if (maybe.isPresent()) {
                User a = maybe.get();
                a.setPassword(passwordEncoder.encode(adminPwd));
                userRepo.save(a);
                System.out.println("Updated 'admin' password from ADMIN_PASSWORD env var.");
            } else {
                User admin = new User("admin", passwordEncoder.encode(adminPwd), Set.of("ROLE_ADMIN", "ROLE_MANAGER"));
                userRepo.save(admin);
                System.out.println("Created admin user from ADMIN_PASSWORD env var.");
            }
        } else {
            // If no env var, skip creating default weak admin to force using secrets.
            if (userRepo.findByUsername("admin").isEmpty()) {
                System.out.println("No ADMIN_PASSWORD set — no default admin created. Please set ADMIN_PASSWORD secret to seed an admin.");
            }
        }
    }
}
