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
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "must not be blank")
    private String title;;

    @Column(nullable = false)
    @NotBlank(message = "must not be blank")
    private String author;
    @Min(value = 0, message = "must be 0 or more")
    private int availableCopies;

}
