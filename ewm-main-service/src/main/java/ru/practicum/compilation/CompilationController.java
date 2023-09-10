package ru.practicum.compilation;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompilationController {
    private final CompilationService service;

    @PostMapping("admin/compilations")
    public CompilationDto add(@RequestBody NewCompilationDto dto) {
        throw new RuntimeException("Метод не реализован");
//        var startDate = LocalDateTime.parse(startDateString, formatter);
//        var endDate = LocalDateTime.parse(endDateString, formatter);
//        unique = unique != null && unique;
//
//        return service.getAllByParams(startDate, endDate, uris, unique);
    }

    @DeleteMapping("admin/compilations/{id}")
    public void delete(@PathVariable Long id) {
        //        service.create(dto);
        //        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PatchMapping("admin/compilations/{id}")
    public CompilationDto update(@PathVariable Long id,
                       @RequestBody UpdateCompilationRequest request) {
        throw new RuntimeException("Метод не реализован");
        //        service.create(dto);
        //        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAll(@RequestParam Boolean pinned,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        throw new RuntimeException("Метод не реализован");
        //        service.create(dto);
        //        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/compilations/{id}")
    public CompilationDto getById(@PathVariable Long id) {
        throw new RuntimeException("Метод не реализован");
        //        service.create(dto);
        //        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
