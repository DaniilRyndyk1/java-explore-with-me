package ru.practicum.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.location.model.Location;

import java.util.Optional;

@Transactional
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findOneByLatAndLon(Float lat, Float lon);
}
