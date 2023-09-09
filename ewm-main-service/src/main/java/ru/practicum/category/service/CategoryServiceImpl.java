package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.handler.NotFoundException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryDto create(@NotNull NewCategoryDto dto) {
        var category = mapper.toCategory(-1L, dto);
        category = repository.save(category);
        return mapper.toCategoryDto(category);
    }

    public void delete(@NotNull Long categoryId) {
        var category = repository.getById(categoryId);
        repository.delete(category);
    }

    public CategoryDto update(@NotNull Long categoryId, @NotNull CategoryDto dto) {
        var categoryOptional = repository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException("Категория не найдена");
        }
        var category = mapper.toCategory(categoryId, dto);
        category = repository.save(category);
        return mapper.toCategoryDto(category);
    }

    public List<CategoryDto> getAll(@NotNull Integer from, @NotNull Integer size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Неверное значение параметра");
        }

        var pageRequest = PageRequest.of(from / size, size);
        return repository
                .findAll(pageRequest)
                .stream()
                .map(mapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getById(@NotNull Long categoryId) {
        var category = repository.getById(categoryId);
        return mapper.toCategoryDto(category);
    }
}