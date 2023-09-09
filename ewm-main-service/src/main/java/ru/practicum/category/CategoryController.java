package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Valid
public class CategoryController {
    private final CategoryService service;

    @PostMapping("admin/categories")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryDto create(@RequestBody NewCategoryDto dto) {
        return service.create(dto);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long categoryId) {
        service.delete(categoryId);
    }

    @PatchMapping("admin/categories/{categoryId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoryDto update(@PathVariable Long categoryId,
                              @RequestBody CategoryDto dto) {
        return service.update(categoryId, dto);
    }

    @GetMapping("/categories")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return service.getAll(from, size);
    }

    @GetMapping("/categories/{categoryId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoryDto getById(@PathVariable Long categoryId) {
        return service.getById(categoryId);
    }
}