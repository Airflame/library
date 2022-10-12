package com.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Category entity class
 *
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String name;
    @Column(name = "is_starting", nullable = false)
    private Boolean isStarting;

    @OneToMany
    @JoinColumn(name="category_id")
    private Set<Book> books;
}
