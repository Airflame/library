package com.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Book entity class
 *
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String author;
    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String title;
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;
    @Column(name = "is_lent", nullable = false)
    private Boolean isLent;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private Set<Lending> lendings;
}
