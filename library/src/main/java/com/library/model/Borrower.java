package com.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "first_name", nullable = false)
    @Size(min = 3, max = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @Size(min = 3, max = 50)
    private String lastName;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "borrower_id")
    private Set<Lending> lendings;
}
