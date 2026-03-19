package ru.vitrailclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vitrailclinic.model.CaseEntity;

public interface CaseRepository extends JpaRepository<CaseEntity, Long> {
}
