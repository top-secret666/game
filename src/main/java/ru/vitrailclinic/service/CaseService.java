package ru.vitrailclinic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.vitrailclinic.dto.CaseRequest;
import ru.vitrailclinic.dto.CaseResponse;
import ru.vitrailclinic.model.CaseEntity;
import ru.vitrailclinic.repository.CaseRepository;

import java.util.NoSuchElementException;

@Service
public class CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseService.class);

    private final CaseRepository repository;

    public CaseService(CaseRepository repository) {
        this.repository = repository;
    }

    public CaseResponse createCase(CaseRequest req) {
        // CWE-778: log security-relevant data modification at creation time
        log.info("Creating case: patientAlias='{}', ageRange='{}', confidentialityLevel='{}'",
                req.getPatientAlias(), req.getAgeRange(), req.getConfidentialityLevel());
        CaseEntity e = new CaseEntity();
        e.setPatientAlias(req.getPatientAlias());
        e.setAgeRange(req.getAgeRange());
        e.setConfidentialityLevel(req.getConfidentialityLevel());
        // status and createdAt set by @PrePersist
        CaseEntity saved = repository.save(e);
        // CWE-778: log outcome with assigned id for audit trail
        log.info("Case created successfully: id={}, confidentialityLevel='{}'",
                saved.getId(), saved.getConfidentialityLevel());
        return toDto(saved);
    }

    public CaseResponse getCase(Long id) {
        log.debug("Fetching case: id={}", id);
        CaseEntity e = repository.findById(id).orElseThrow(() -> {
            // CWE-778: log when a requested resource is not found — aids in detecting enumeration
            log.warn("Case not found: id={}", id);
            return new NoSuchElementException("Case not found");
        });
        return toDto(e);
    }

    private CaseResponse toDto(CaseEntity e) {
        CaseResponse r = new CaseResponse();
        r.setId(e.getId());
        r.setStatus(e.getStatus());
        r.setPatientAlias(e.getPatientAlias());
        r.setAgeRange(e.getAgeRange());
        r.setConfidentialityLevel(e.getConfidentialityLevel());
        r.setCreatedAt(e.getCreatedAt());
        return r;
    }
}
