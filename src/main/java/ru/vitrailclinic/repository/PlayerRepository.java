package ru.vitrailclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vitrailclinic.model.PlayerEntity;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findByUsername(String username);

    Optional<PlayerEntity> findByEmail(String email);

    boolean existsByUsername(String username);
}
