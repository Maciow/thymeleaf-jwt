package com.security.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long id;

    @Column(nullable = false, unique = true)
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(id, userRole.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
