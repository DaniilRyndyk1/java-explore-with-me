package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto dto);
    void delete(Long categoryId);
    CategoryDto update(Long categoryId, CategoryDto dto);
    List<CategoryDto> getAll(Integer from, Integer size);
    CategoryDto getDtoById(Long categoryId);
}
