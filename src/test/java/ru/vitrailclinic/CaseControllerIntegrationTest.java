package ru.vitrailclinic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.vitrailclinic.controller.ApiError;
import ru.vitrailclinic.dto.CaseRequest;
import ru.vitrailclinic.dto.CaseResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests — full Spring application context + real PostgreSQL via Testcontainers.
 *
 * Strategy:
 *  - @SpringBootTest starts the whole app on a random port.
 *  - @Testcontainers + @ServiceConnection spins up a PostgreSQL container and auto-wires
 *    the datasource; Flyway runs migrations on container startup.
 *  - @ActiveProfiles("test") activates application-test.yml which supplies placeholder
 *    credentials so that ${DB_USERNAME}/${DB_PASSWORD} in application.yml never panic.
 *  - TestRestTemplate sends real HTTP requests through the full request/response pipeline.
 *
 * These tests are intentionally heavier — reserve them for CI pipelines.
 * Unit tests and slice tests cover the same logic at lower cost.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class CaseControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    // ──────────────────────────────────────────────────────────────────────────
    // happy path
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Integration: POST /api/cases then GET returns identical case")
    void createAndGetCase_fullRoundTrip() {
        CaseRequest req = request("IntegrationAlias", "25-35", "MEDIUM");

        // create
        ResponseEntity<CaseResponse> createResp =
                restTemplate.postForEntity("/api/cases", req, CaseResponse.class);

        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CaseResponse created = createResp.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getId()).isPositive();
        assertThat(created.getStatus()).isEqualTo("DRAFT");
        assertThat(created.getPatientAlias()).isEqualTo("IntegrationAlias");
        assertThat(created.getAgeRange()).isEqualTo("25-35");
        assertThat(created.getConfidentialityLevel()).isEqualTo("MEDIUM");
        assertThat(created.getCreatedAt()).isNotNull();

        // read back
        ResponseEntity<CaseResponse> getResp =
                restTemplate.getForEntity("/api/cases/" + created.getId(), CaseResponse.class);

        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        CaseResponse fetched = getResp.getBody();
        assertThat(fetched).isNotNull();
        assertThat(fetched.getId()).isEqualTo(created.getId());
        assertThat(fetched.getPatientAlias()).isEqualTo("IntegrationAlias");
        assertThat(fetched.getConfidentialityLevel()).isEqualTo("MEDIUM");
        assertThat(fetched.getCreatedAt()).isNotNull();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // validation
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Integration: POST /api/cases with blank alias → 400 Bad Request")
    void createCase_blankAlias_returns400() {
        CaseRequest req = request("", "20-30", "LOW");

        ResponseEntity<ApiError> resp =
                restTemplate.postForEntity("/api/cases", req, ApiError.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getStatus()).isEqualTo(400);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // not found
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Integration: GET /api/cases/{id} non-existent id → 404 Not Found")
    void getCase_nonExistentId_returns404() {
        ResponseEntity<ApiError> resp =
                restTemplate.getForEntity("/api/cases/99999999", ApiError.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getStatus()).isEqualTo(404);
        assertThat(resp.getBody().getMessage()).isEqualTo("Case not found");
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
}
