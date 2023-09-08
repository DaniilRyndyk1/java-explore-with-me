package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Valid
public class CategoryController {
    private final CategoryService service;

    @PostMapping("admin/categories")
    public CategoryDto add(@RequestBody NewCategoryDto dto) {
        return service.add(dto);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public void delete(@PathVariable Long categoryId) {
        service.delete(categoryId);
    }

    @PatchMapping("admin/categories/{categoryId}")
    public CategoryDto update(@PathVariable Long categoryId,
                              @RequestBody CategoryDto dto) {
        return service.update(categoryId, dto);
    }

    @GetMapping("/categories")
    public Page<CategoryDto> getAll(@RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return service.getAll(from, size);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getById(@PathVariable Long categoryId) {
        return service.getById(categoryId);
    }
}