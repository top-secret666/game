package ru.vitrailclinic.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.vitrailclinic.dto.PlayerRequest;
import ru.vitrailclinic.dto.PlayerResponse;
import ru.vitrailclinic.dto.PlayerRequest;
import ru.vitrailclinic.dto.PlayerResponse;
import ru.vitrailclinic.service.PlayerService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService service;

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