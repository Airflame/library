package com.library.service;

import com.library.dto.StatisticsDTO;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class StatisticsService {

    private final BookRepository bookRepository;

    private final LendingService lendingService;

    private final CategoryService categoryService;

    public StatisticsDTO getStatistics() {

        return StatisticsDTO.builder()
                .availableBooks(bookRepository.countByIsLent(false))
                .lentBooks(bookRepository.countByIsLent(true))
                .lendingTimeline(lendingService.countByMonths())
                .categories(categoryService.getAllCategoriesWithBookCount())
                .build();
    }
}
