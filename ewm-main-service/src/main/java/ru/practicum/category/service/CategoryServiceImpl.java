package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.Utils;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
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

    public CategoryDto getDtoById(@NotNull Long id) {
        return mapper.toCategoryDto(getById(id));
    }

    public Category getById(@NotNull Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id=" + id + " was not found")
        );
    }

    public CategoryDto create(@NotNull NewCategoryDto dto) {
        return mapper.toCategoryDto(
                repository.save(
                        mapper.toCategory(-1L, dto)
                )
        );
    }

    public void delete(@NotNull Long categoryId) {
        repository.delete(
                repository.getById(categoryId)
        );
    }

    public CategoryDto update(@NotNull Long id, @NotNull CategoryDto dto) {
        getById(id);
        return mapper.toCategoryDto(
                repository.save(
                        new Category(id, dto.getName())
                )
        );
    }

    public List<CategoryDto> getAll(@NotNull Integer from, @NotNull Integer size) {
        var request = Utils.getPageRequest(from, size);
        return repository
                .findAll(request)
                .stream()
                .map(mapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}