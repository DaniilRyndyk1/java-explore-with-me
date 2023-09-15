package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CompilationService {
    CompilationDto add(@NotNull NewCompilationDto dto);
    void delete(@NotNull Long id);
    CompilationDto update(@NotNull Long id, @NotNull UpdateCompilationRequest request);
    List<CompilationDto> getAll(@NotNull Boolean pinned, @NotNull Integer from, @NotNull Integer size);
    CompilationDto getDtoById(@NotNull Long id);
    Compilation getById(@NotNull Long id);
}
