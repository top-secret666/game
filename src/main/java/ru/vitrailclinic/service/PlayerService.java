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

    // TODO: конструктор с двумя параметрами (Constructor Injection)
    public PlayerService(PlayerRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
        public PlayerResponse createPlayer(RegisterRequest req) {
            // TODO 1: проверь, что username ещё не занят
            //         если занят — брось IllegalArgumentException("Username already taken")
            if (repository.existsByUsername(req.getUsername())) {
                throw new IllegalArgumentException("Username already taken");
            }
            // TODO 2: создай PlayerEntity
            PlayerEntity player = new PlayerEntity();
            player.setUsername(req.getUsername());
        
            // TODO 3: установи passwordHash через passwordEncoder.encode(req.getPassword())
            player.setPasswordHash(passwordEncoder.encode(req.getPassword()));

            // TODO 4: сохрани через repository.save(...)
            PlayerEntity savedPlayer = repository.save(player);

            // TODO 5: преобразуй в PlayerResponse и верни
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

       return response;
    }
    
 public PlayerResponse getPlayer(Long id) {
    PlayerEntity player = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + id));

    return toDto(player);
}

}