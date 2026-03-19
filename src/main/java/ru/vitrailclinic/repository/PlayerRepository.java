package ru.vitrailclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vitrailclinic.model.PlayerEntity;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
}
