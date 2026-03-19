package ru.vitrailclinic.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.vitrailclinic.dto.RegisterRequest;
import ru.vitrailclinic.dto.PlayerResponse;
import ru.vitrailclinic.service.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService service) { this.service = service; }

    @PostMapping("/register")
    public ResponseEntity<PlayerResponse> createPlayer(@Valid @RequestBody RegisterRequest req) {
        PlayerResponse res = service.createPlayer(req);
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable Long id) {
        PlayerResponse res = service.getPlayer(id);
        return ResponseEntity.ok(res);
    }

}