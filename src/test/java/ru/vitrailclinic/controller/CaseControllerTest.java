package ru.vitrailclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.vitrailclinic.dto.CaseRequest;
import ru.vitrailclinic.dto.CaseResponse;
import ru.vitrailclinic.service.CaseService;

import java.time.Instant;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web-layer slice tests for {@link CaseController}.
 *
 * Strategy: load only the web layer (controller + GlobalExceptionHandler + Jackson).
 * The service is replaced by a Mockito mock — no DB, no Flyway, runs fast.
 *
 * Covers:
 *  - HTTP status codes
 *  - Bean Validation (400 responses)
 *  - Response body shape
 *  - GlobalExceptionHandler error envelope
 */
@WebMvcTest(CaseController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class CaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaseService service;

    @Autowired
    private ObjectMapper objectMapper;

    // ──────────────────────────────────────────────────────────────────────────
    // POST /api/cases — happy path
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/cases valid request → 201 with full response body")
    void createCase_validRequest_returns201WithBody() throws Exception {
        CaseRequest req = request("Alice", "30-40", "CONFIDENTIAL");
        CaseResponse resp = response(1L, "DRAFT", "Alice", "30-40", "CONFIDENTIAL");
        when(service.createCase(any())).thenReturn(resp);

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.patientAlias").value("Alice"))
                .andExpect(jsonPath("$.ageRange").value("30-40"))
                .andExpect(jsonPath("$.confidentialityLevel").value("CONFIDENTIAL"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // POST /api/cases — validation failures (400)
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/cases blank patientAlias → 400 with error array")
    void createCase_blankPatientAlias_returns400() throws Exception {
        CaseRequest req = request("", "20-30", "LOW");

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("POST /api/cases whitespace-only patientAlias → 400 (@NotBlank rejects blanks)")
    void createCase_whitespaceOnlyAlias_returns400() throws Exception {
        CaseRequest req = request("   ", "20-30", "LOW");

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/cases patientAlias = 101 chars → 400 (@Size max=100)")
    void createCase_aliasTooLong_returns400() throws Exception {
        CaseRequest req = request("A".repeat(101), "20-30", "LOW");

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/cases null ageRange → 400 (@NotBlank)")
    void createCase_nullAgeRange_returns400() throws Exception {
        // null fields serialise as absent JSON keys → @NotBlank fires
        CaseRequest req = request("Alice", null, "LOW");

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/cases null confidentialityLevel → 400 (@NotBlank)")
    void createCase_nullConfidentialityLevel_returns400() throws Exception {
        CaseRequest req = request("Alice", "20-30", null);

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // GET /api/cases/{id}
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/cases/{id} existing id → 200 with response body")
    void getCase_existingId_returns200WithBody() throws Exception {
        CaseResponse resp = response(5L, "DRAFT", "Bob", "40-50", "MEDIUM");
        when(service.getCase(5L)).thenReturn(resp);

        mockMvc.perform(get("/api/cases/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.patientAlias").value("Bob"))
                .andExpect(jsonPath("$.ageRange").value("40-50"))
                .andExpect(jsonPath("$.confidentialityLevel").value("MEDIUM"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    @DisplayName("GET /api/cases/{id} non-existent id → 404 with ApiError body")
    void getCase_nonExistentId_returns404WithErrorBody() throws Exception {
        when(service.getCase(999L)).thenThrow(new NoSuchElementException("Case not found"));

        mockMvc.perform(get("/api/cases/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Case not found"))
                .andExpect(jsonPath("$.errors").isArray());
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

    private static CaseResponse response(Long id, String status, String alias, String age, String level) {
        CaseResponse r = new CaseResponse();
        r.setId(id);
        r.setStatus(status);
        r.setPatientAlias(alias);
        r.setAgeRange(age);
        r.setConfidentialityLevel(level);
        r.setCreatedAt(Instant.now());
        return r;
    }
}
