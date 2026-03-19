package ru.vitrailclinic.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vitrailclinic.dto.CaseRequest;
import ru.vitrailclinic.dto.CaseResponse;
import ru.vitrailclinic.service.CaseService;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService service;

    public CaseController(CaseService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CaseResponse> createCase(@Valid @RequestBody CaseRequest req) {
        CaseResponse res = service.createCase(req);
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaseResponse> getCase(@PathVariable Long id) {
        CaseResponse res = service.getCase(id);
        return ResponseEntity.ok(res);
    }
}
