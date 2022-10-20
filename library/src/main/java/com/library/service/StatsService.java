package com.library.service;

import com.library.dto.StatsBooksAvailabilityDTO;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class StatsService {

    private final BookRepository bookRepository;

    public StatsBooksAvailabilityDTO getBooksAvailability() {
        return StatsBooksAvailabilityDTO.builder()
                .available(bookRepository.countByIsLent(false))
                .lent(bookRepository.countByIsLent(true))
                .build();
    }
}
