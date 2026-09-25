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
        // Seed or update an admin user using environment secrets: ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_ROLES
        String adminUser = System.getenv("ADMIN_USERNAME");
        if (adminUser == null || adminUser.isBlank()) adminUser = "admin";
        String adminPwd = System.getenv("ADMIN_PASSWORD");
        String adminRolesEnv = System.getenv("ADMIN_ROLES");

        if (adminPwd != null && !adminPwd.isBlank()) {
            java.util.Set<String> roles;
            if (adminRolesEnv != null && !adminRolesEnv.isBlank()) {
                roles = java.util.Arrays.stream(adminRolesEnv.split(","))
                        .map(String::trim).filter(s->!s.isEmpty()).collect(java.util.stream.Collectors.toSet());
            } else {
                roles = Set.of("ROLE_ADMIN","ROLE_MANAGER");
            }

            var maybe = userRepo.findByUsername(adminUser);
            if (maybe.isPresent()) {
                User a = maybe.get();
                a.setPassword(passwordEncoder.encode(adminPwd));
                a.setRoles(roles);
                userRepo.save(a);
                System.out.println("Updated '"+adminUser+"' password and roles from ADMIN_* env vars.");
            } else {
                User admin = new User(adminUser, passwordEncoder.encode(adminPwd), roles);
                userRepo.save(admin);
                System.out.println("Created admin user '"+adminUser+"' from ADMIN_* env vars.");
            }
        } else {
            // If no ADMIN_PASSWORD provided, do not create a default weak admin; require secret provisioning.
            if (userRepo.findByUsername("admin").isEmpty()) {
                System.out.println("No ADMIN_PASSWORD set — no default admin created. Set ADMIN_USERNAME and ADMIN_PASSWORD secrets to seed an admin user.");
            }
        }
    }
}
