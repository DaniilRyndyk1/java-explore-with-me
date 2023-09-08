package ru.practicum.category.service;

import org.springframework.data.domain.Page;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

public interface CategoryService {
    CategoryDto add(NewCategoryDto dto);
    void delete(Long categoryId);
    CategoryDto update(Long categoryId, CategoryDto dto);
    Page<CategoryDto> getAll(Integer from, Integer size);
    CategoryDto getById(Long categoryId);
}
