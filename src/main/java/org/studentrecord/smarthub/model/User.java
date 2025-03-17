package org.studentrecord.smarthub.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.Setter;
import lombok.Getter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    private Role role;
}
