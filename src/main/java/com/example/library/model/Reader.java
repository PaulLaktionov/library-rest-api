package com.example.library.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@Setter
@Entity
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "must not be blank")
    private String name;

    @NotBlank(message = "must not be blank")
    @Email(message = "must be a valid email")
    @Column(nullable = false, unique = true)
    private String email;


}
