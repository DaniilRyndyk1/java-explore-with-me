package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryDto create(NewCategoryDto dto) {
        var category = mapper.toCategory(-1L, dto);
        category = repository.save(category);
        return mapper.toCategoryDto(category);
    }

    public void delete(Long categoryId) {
        var category = repository.getById(categoryId);
        repository.delete(category);
    }

    public CategoryDto update(Long categoryId, CategoryDto dto) {
        var category = mapper.toCategory(categoryId, dto);
        category = repository.save(category);
        return mapper.toCategoryDto(category);
    }

    public Page<CategoryDto> getAll(Integer from, Integer size) {
        var pageRequest = PageRequest.of(from / size, size);
        return repository.findAllBy(pageRequest);
    }

    public CategoryDto getById(Long categoryId) {
        var category = repository.getById(categoryId);
        return mapper.toCategoryDto(category);
    }
}