package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CategoryController {
    private final CategoryService service;

    @PostMapping("admin/categories")
    public CategoryDto add(@RequestBody NewCategoryDto dto) {
        throw new RuntimeException("Метод не реализован");
//        var startDate = LocalDateTime.parse(startDateString, formatter);
//        var endDate = LocalDateTime.parse(endDateString, formatter);
//        unique = unique != null && unique;
//
//        return service.getAllByParams(startDate, endDate, uris, unique);
    }

    @DeleteMapping("admin/categories/{catId}")
    public void delete(@PathVariable Long catId) {
        //        service.create(dto);
        //        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PatchMapping("admin/categories/{catId}")
    public CategoryDto update(@PathVariable Long catId,
                       @RequestBody CategoryDto dto) {
        throw new RuntimeException("Метод не реализован");
        //        service.create(dto);
        //        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        throw new RuntimeException("Метод не реализован");
        //        service.create(dto);
        //        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getById(@PathVariable Long catId) {
        throw new RuntimeException("Метод не реализован");
        //        service.create(dto);
        //        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}