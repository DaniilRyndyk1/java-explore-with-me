package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT count(e.id) " +
            "FROM Event e " +
            "WHERE e.category.id = :categoryId")
    Long countEventsByCategory(Long categoryId);
}