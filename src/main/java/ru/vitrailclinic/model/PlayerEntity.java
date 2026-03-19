 package ru.vitrailclinic.model;
    
    import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
    
    @Entity
    @Table(name = "players")
    public class PlayerEntity {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        // TODO: добавь аннотации — поле уникально и обязательно, макс 50 символов
        @Column(unique = true)
        @Size(min = 3, max = 50)
        private String username;
    
        // TODO: добавь аннотации — поле уникально и обязательно
        @Column(unique = true)
        private String email;
    
        @Column(name = "password_hash", nullable = false)
        private String passwordHash;
    
        // TODO: добавь @Column с default значением 1, nullable = false
        private int level;
    
        // TODO: добавь @Column с default значением 0, nullable = false
        @Column ( nullable = false,columnDefinition = "int default 0")
        private long experience;
    
        // TODO: что за аннотации нужны для хранения строки-перечисления (APPRENTICE, JOURNEYMAN...)?
        @NotNull(message = "The rank must be specified")
        @Column(nullable = false)                   
        @Enumerated(EnumType.STRING) 
        private PlayerRank rank;
    
        @Column(name = "created_at", nullable = false, updatable = false)
        private Instant createdAt;
    
        // TODO: метод @PrePersist — установи значения по умолчанию
        // level=1, experience=0, rank=APPRENTICE, createdAt=Instant.now()
        @PrePersist
        public void onCreate() {
            
        this.createdAt = Instant.now();
    
         if (this.level == 0) {
            this.level = 1; 
        }
        
        if (this.experience == 0) {
            this.experience = 0; 
        }
        
        if (this.rank == null) {
            this.rank = PlayerRank.APPRENTICE; 
        }
        }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public Long getExperience() { return experience; }
    public void setExperience(Long experience) { this.experience = experience; }
    
    public PlayerRank getRank() { return rank; }
    public void setRank(PlayerRank rank) { this.rank = rank; }

    }
      
    

