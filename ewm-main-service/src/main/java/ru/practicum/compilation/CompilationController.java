package ru.practicum.compilation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompilationController {
    private final CompilationService service;

    @GetMapping("/compilations/{id}")
    public CompilationDto getById(@PathVariable Long id) {
        return service.getDtoById(id);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return service.getAll(pinned, from, size);
    }

    @PostMapping("admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto dto) {
        return service.create(dto);
    }

    @DeleteMapping("admin/compilations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping("admin/compilations/{id}")
    public CompilationDto update(@PathVariable Long id,
                                 @Valid @RequestBody UpdateCompilationRequest request) {
        return service.update(id, request);
    }
}