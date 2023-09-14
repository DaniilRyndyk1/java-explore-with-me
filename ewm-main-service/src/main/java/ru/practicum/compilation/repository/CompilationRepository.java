package ru.practicum.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.model.Compilation;

@Transactional
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}