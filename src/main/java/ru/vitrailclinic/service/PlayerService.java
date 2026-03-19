package ru.vitrailclinic.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.vitrailclinic.model.CaseEntity;
import ru.vitrailclinic.model.PlayerEntity;
import ru.vitrailclinic.repository.PlayerRepository;
import ru.vitrailclinic.dto.PlayerResponse;
import ru.vitrailclinic.dto.RegisterRequest;

@Service
public class PlayerService {

    private final PlayerRepository repository;
    private final PasswordEncoder passwordEncoder;

    // TODO: конструктор с двумя параметрами (Constructor Injection)
    public PlayerService(PlayerRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
        public PlayerResponse register(RegisterRequest req) {
            // TODO 1: проверь, что username ещё не занят
            //         если занят — брось IllegalArgumentException("Username already taken")
            if (repository.existsByUsername(req.getUsername())) {
                throw new IllegalArgumentException("Username already taken");
            }
            // TODO 2: создай PlayerEntity
            PlayerEntity player = new PlayerEntity(player.setUsername(req.getUsername()));
        }
            // TODO 3: установи passwordHash через passwordEncoder.encode(req.getPassword())
            player.setPasswordHash(passwordEncoder.encode(req.getPassword()));

            // TODO 4: сохрани через repository.save(...)
            PlayerEntity savedPlayer = repository.save(player);

            // TODO 5: преобразуй в PlayerResponse и верни

            PlayerResponse response = new PlayerResponse();

            response.setId(savedPlayer.getId());
            response.setUsername(savedPlayer.getUsername());

            return response;
        
       private PlayerResponse toDto(PlayerEntity e) {
       PlayerResponse response = new PlayerResponse();

        response.setId(e.getId());
        response.setUsername(e.getUsername());
    return response;
    }
    
    public PlayerResponse register(RegisterRequest req) {
        // 1. Проверка
        if (repository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

    // 2. Создание и заполнение (не забудь имя!)
    PlayerEntity player = new PlayerEntity();
    player.setUsername(req.getUsername()); // Добавили имя
    
    // 3. Пароль
    player.setPasswordHash(passwordEncoder.encode(req.getPassword()));

    // 4. Сохранение
    PlayerEntity savedPlayer = repository.save(player);

    // 5. Преобразование через твой метод toDto
    return toDto(savedPlayer); 
}


}