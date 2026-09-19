package com.expensetracker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.expensetracker.entity.Category;
import com.expensetracker.repository.CategoryRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedCategories(CategoryRepository categoryRepository) {
        return args -> {

            if (categoryRepository.count() == 0) {

                categoryRepository.save(new Category("Food"));
                categoryRepository.save(new Category("Travel"));
                categoryRepository.save(new Category("Skincare"));
                categoryRepository.save(new Category("Stationery"));
                categoryRepository.save(new Category("Shopping"));
                categoryRepository.save(new Category("Other"));

            }
        };
    }
}