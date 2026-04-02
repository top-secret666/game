package ru.vitrailclinic.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.vitrailclinic.model.PlayerEntity;
import ru.vitrailclinic.repository.PlayerRepository;
import ru.vitrailclinic.dto.RegisterRequest;
import ru.vitrailclinic.dto.PlayerResponse;

@Service
public class PlayerService {

    private final PlayerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public PlayerService(PlayerRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
        public PlayerResponse createPlayer(RegisterRequest req) {
            if (repository.existsByUsername(req.getUsername())) {
                throw new IllegalArgumentException("Username already taken");
            }
            PlayerEntity player = new PlayerEntity();
            player.setUsername(req.getUsername());
        
            player.setEmail(req.getEmail());

            player.setPasswordHash(passwordEncoder.encode(req.getPassword()));

            PlayerEntity savedPlayer = repository.save(player);

            return toDto(savedPlayer);
        }


       private PlayerResponse toDto(PlayerEntity e) {
       PlayerResponse response = new PlayerResponse();
        response.setId(e.getId());
        response.setUsername(e.getUsername());
        response.setEmail(e.getEmail());
        response.setLevel(e.getLevel());
        response.setExperience(e.getExperience());
        response.setRank(e.getRank());
        response.setCreatedAt(e.getCreatedAt());
       return response;
    }
    
 public PlayerResponse getPlayer(Long id) {
    PlayerEntity player = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + id));

    return toDto(player);
}

}