package org.studentrecord.smarthub.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.Setter;
import lombok.Getter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
