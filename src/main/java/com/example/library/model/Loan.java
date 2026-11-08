package com.example.library.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@Setter
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "is required")
    private Long bookId;
    @NotNull(message = "is required")
    private Long readerId;

    private LocalDateTime loanDate;

    private LocalDateTime returnDate;
}
