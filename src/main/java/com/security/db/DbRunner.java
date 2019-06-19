package com.security.db;

import com.security.model.Permission;
import com.security.model.User;
import com.security.model.UserRole;
import com.security.repository.PermissionRepository;
import com.security.repository.UserRepository;
import com.security.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

//@Service
public class DbRunner implements CommandLineRunner {
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private PermissionRepository permissionRepository;
    private PasswordEncoder passwordEncoder;

    public DbRunner(UserRepository userRepository, UserRoleRepository userRoleRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userRepository.deleteAll();
        UserRole admin = UserRole.builder()
                .role("ROLE_ADMIN")
                .build();

        UserRole user = UserRole.builder()
                .role("ROLE_USER")
                .build();

        UserRole manager = UserRole.builder()
                .role("ROLE_MANAGER")
                .build();

        Permission access_test1= Permission.builder()
                .permission("ACCESS_TEST1")
                .build();

        Permission access_test2 = Permission.builder()
                .permission("ACCESS_TEST2")
                .build();

        User adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singleton(admin))
                .permissions(new HashSet<>(Arrays.asList(access_test1, access_test2)))
                .active(true)
                .build();

        User ordinaryUser = User.builder()
                .username("user")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singleton(user))
                .permissions(Collections.emptySet())
                .active(true)
                .build();

        User managerUser = User.builder()
                .username("manager")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singleton(manager))
                .permissions(Collections.singleton(access_test1))
                .active(true)
                .build();

        List<User> users = Arrays.asList(adminUser, ordinaryUser, managerUser);

        this.userRepository.saveAll(users);
    }
}
