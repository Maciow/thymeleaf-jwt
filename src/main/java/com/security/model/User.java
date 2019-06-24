package com.security.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean active;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id_role")})
    private Set<UserRole> roles;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_permissions",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id_user")},
            inverseJoinColumns= {@JoinColumn(name = "permission_id", referencedColumnName = "id_permission")})
    private Set<Permission> permissions;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private Set<Order> orders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
