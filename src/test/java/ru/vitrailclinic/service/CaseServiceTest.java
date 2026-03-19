package ru.vitrailclinic.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vitrailclinic.dto.CaseRequest;
import ru.vitrailclinic.dto.CaseResponse;
import ru.vitrailclinic.model.CaseEntity;
import ru.vitrailclinic.repository.CaseRepository;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CaseService}.
 *
 * Strategy: mock the repository, exercise only the service's own logic.
 * No Spring context is loaded — this runs in milliseconds.
 */
@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

    @Mock
    private CaseRepository repository;

    @InjectMocks
    private CaseService caseService;

    // ──────────────────────────────────────────────────────────────────────────
    // createCase
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("createCase: valid request → entity persisted, DTO fields match")
    void createCase_validRequest_savesEntityAndReturnsDto() {
        CaseRequest req = request("Alice", "30-40", "HIGH");
        when(repository.save(any(CaseEntity.class))).thenAnswer(inv -> {
            CaseEntity e = inv.getArgument(0);
            e.setId(1L);
            e.prePersist();   // simulate JPA @PrePersist lifecycle callback
            return e;
        });

        CaseResponse res = caseService.createCase(req);

        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getStatus()).isEqualTo("DRAFT");
        assertThat(res.getPatientAlias()).isEqualTo("Alice");
        assertThat(res.getAgeRange()).isEqualTo("30-40");
        assertThat(res.getConfidentialityLevel()).isEqualTo("HIGH");
        assertThat(res.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("createCase: entity passed to repository carries all request fields")
    void createCase_entityPassedToRepositoryHasCorrectFields() {
        CaseRequest req = request("Bob", "20-25", "LOW");
        when(repository.save(any(CaseEntity.class))).thenAnswer(inv -> {
            CaseEntity e = inv.getArgument(0);
            e.setId(2L);
            e.prePersist();
            return e;
        });

        caseService.createCase(req);

        ArgumentCaptor<CaseEntity> captor = ArgumentCaptor.forClass(CaseEntity.class);
        verify(repository).save(captor.capture());
        CaseEntity saved = captor.getValue();
        assertThat(saved.getPatientAlias()).isEqualTo("Bob");
        assertThat(saved.getAgeRange()).isEqualTo("20-25");
        assertThat(saved.getConfidentialityLevel()).isEqualTo("LOW");
    }

    @Test
    @DisplayName("createCase: findById is never called (no superfluous queries)")
    void createCase_doesNotIssueAnyFindQuery() {
        when(repository.save(any())).thenAnswer(inv -> {
            CaseEntity e = inv.getArgument(0);
            e.setId(3L);
            e.prePersist();
            return e;
        });

        caseService.createCase(request("Carol", "40-50", "MEDIUM"));

        verify(repository, never()).findById(any());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // getCase
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getCase: existing id → all DTO fields populated correctly")
    void getCase_existingId_returnsPopulatedDto() {
        CaseEntity entity = entity(42L, "DRAFT", "Carol", "50-60", "MEDIUM");
        when(repository.findById(42L)).thenReturn(Optional.of(entity));

        CaseResponse res = caseService.getCase(42L);

        assertThat(res.getId()).isEqualTo(42L);
        assertThat(res.getStatus()).isEqualTo("DRAFT");
        assertThat(res.getPatientAlias()).isEqualTo("Carol");
        assertThat(res.getAgeRange()).isEqualTo("50-60");
        assertThat(res.getConfidentialityLevel()).isEqualTo("MEDIUM");
        assertThat(res.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("getCase: non-existent id → throws NoSuchElementException(\"Case not found\")")
    void getCase_nonExistentId_throwsNoSuchElementException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> caseService.getCase(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Case not found");

        verify(repository).findById(99L);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // test helpers
    // ──────────────────────────────────────────────────────────────────────────

    private static CaseRequest request(String alias, String age, String level) {
        CaseRequest r = new CaseRequest();
        r.setPatientAlias(alias);
        r.setAgeRange(age);
        r.setConfidentialityLevel(level);
        return r;
    }

    private static CaseEntity entity(Long id, String status, String alias, String age, String level) {
        CaseEntity e = new CaseEntity();
        e.setId(id);
        e.setStatus(status);
        e.setPatientAlias(alias);
        e.setAgeRange(age);
        e.setConfidentialityLevel(level);
        e.setCreatedAt(Instant.now());
        return e;
    }
}
